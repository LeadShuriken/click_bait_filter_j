package com.clickbait.plugin.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import com.google.common.base.Strings;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clickbait.plugin.services.ApplicationUserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final String apiSalt;
    private final String authentication;
    private final EncryptionHandlers encryptionHandlers;
    private final ApplicationUserService apiUserService;
    private final AuthenticationManager authenticationManager;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getMethod().equalsIgnoreCase("POST") || !request.getRequestURI().equals(authentication);
    }

    public AuthenticationFilter(AuthenticationManager authenticationManager, ApplicationUserService apiUserService,
            EncryptionHandlers encryptionHandlers, String authentication, String apiSalt) {
        this.apiSalt = apiSalt;
        this.apiUserService = apiUserService;
        this.authenticationManager = authenticationManager;
        this.encryptionHandlers = encryptionHandlers;
        this.authentication = authentication;
    }

    private Authentication authenticate(String username, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken uPassAuthToken = new UsernamePasswordAuthenticationToken(username,
                password);
        uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationManager.authenticate(uPassAuthToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authenticate = null;

        final String remoteAddr = request.getRemoteAddr();
        final String username = encryptionHandlers.getMacPasswordEncoder(apiSalt).encode(remoteAddr);
        final String token = encryptionHandlers.getAuthHeader(request);

        if (Strings.isNullOrEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // What it takes
        try {
            if (encryptionHandlers.validateToken(token, username)) {
                UserDetails userDetails = apiUserService.loadUserByUsername(username);
                authenticate = authenticate(username, userDetails.getPassword(), request);
            }
        } catch (ExpiredJwtException e) {
            try {
                UserDetails userDetails = apiUserService.loadUserByUsername(e.getClaims().getSubject());
                authenticate = authenticate(userDetails.getUsername(), userDetails.getPassword(), request);
            } catch (Exception ex) {
                final String password = encryptionHandlers.getMacPasswordEncoder(remoteAddr).encode(token);
                try {
                    authenticate = authenticate(username, password, request);
                } catch (Exception exc) {
                    apiUserService.loadOrCreateUserByUsernamePassword(username, password);
                    authenticate = authenticate(username, password, request);
                }
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwt = encryptionHandlers.createToken(authenticate);

        response.setHeader(encryptionHandlers.getAuthHeader(), encryptionHandlers.getTokenPrefix() + " " + jwt);

        filterChain.doFilter(request, response);
    }
}
