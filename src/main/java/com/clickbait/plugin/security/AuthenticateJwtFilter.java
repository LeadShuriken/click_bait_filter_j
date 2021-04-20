package com.clickbait.plugin.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import com.google.common.base.Strings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clickbait.plugin.services.ApplicationUserService;

import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class AuthenticateJwtFilter extends OncePerRequestFilter {

    private final String apiSalt;
    private final String processing;
    private final ApplicationUserService apiUserService;
    private final EncryptionHandlers encryptionHandlers;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getMethod().equalsIgnoreCase("POST") || !request.getRequestURI().matches(processing)
                || isAuthenticated();
    }

    public AuthenticateJwtFilter(ApplicationUserService apiUserService, EncryptionHandlers encryptionHandlers,
            String processing, String apiSalt) {
        this.apiSalt = apiSalt;
        this.apiUserService = apiUserService;
        this.encryptionHandlers = encryptionHandlers;
        this.processing = processing;
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String remoteAddr = request.getRemoteAddr();
        final String username = encryptionHandlers.getMacPasswordEncoder(apiSalt).encode(remoteAddr);
        final String token = encryptionHandlers.getAuthHeader(request);

        if (Strings.isNullOrEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (encryptionHandlers.validateToken(token, username)) {
                UserDetails userDetails = apiUserService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken uPassAuthToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(uPassAuthToken);
            }
        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }

        filterChain.doFilter(request, response);
    }
}
