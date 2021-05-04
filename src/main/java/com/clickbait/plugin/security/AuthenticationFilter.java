package com.clickbait.plugin.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import com.google.common.base.Strings;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clickbait.plugin.dao.User;
import com.clickbait.plugin.services.ApplicationUserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final String authentication;
    private final String adminAuthentication;
    private final EncryptionHandlers security;
    private final ApplicationUserService apiUserService;
    private final AuthenticationManager authenticationManager;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getRequestURI().matches(authentication)
                && !request.getRequestURI().matches(adminAuthentication);
    }

    public AuthenticationFilter(AuthenticationManager authenticationManager, ApplicationUserService apiUserService,
            EncryptionHandlers security, String authentication, String adminAuthentication) {
        this.apiUserService = apiUserService;
        this.authenticationManager = authenticationManager;
        this.security = security;
        this.authentication = authentication;
        this.adminAuthentication = adminAuthentication;
    }

    private Authentication authenticate(String username, String password, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken uPassAuthToken = new UsernamePasswordAuthenticationToken(username,
                password);
        uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationManager.authenticate(uPassAuthToken);
    }

    private Authentication authenticate(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken uPassAuthToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword());
        uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationManager.authenticate(uPassAuthToken);
    }

    private Authentication passwordVer(AuthenticatedUser userDetails, HttpServletRequest request) {
        if (userDetails.getRole() == ApplicationUserRole.ADMIN) {
            User adm = security.getAdminFromHeader(request);
            // No admin token rebuilds without pass
            if (adm.getName().equals(userDetails.getUsername())
                    && security.pbkdf2Matches(adm.getPassword(), userDetails.getPassword())) {
                return authenticate(userDetails, request);
            }
        } else if (security.pbkdf2Matches(userDetails.getUsername(), userDetails.getPassword())) {
            return authenticate(userDetails, request);
        }
        return null;
    }

    private Authentication createWorkflow(String addr, String token, HttpServletRequest request) {
        String username = security.getMacPasswordEncoder(addr).encode(token);
        String password = security.pbkdf2Hash(username);
        try {
            return authenticate(username, password, request);
        } catch (InternalAuthenticationServiceException | BadCredentialsException
                | EmptyResultDataAccessException exc) {
            UserDetails a = apiUserService.loadOrCreateUserByUsernamePassword(username, password);
            return authenticate(a.getUsername(), a.getPassword(), request);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authenticate = null;
        security.getMacPasswordEncoder(security.getPasswordEncoder().getSalt());

        if (request.getRequestURI().matches(adminAuthentication)) {
            User adm = security.getAdminFromHeader(request);
            UserDetails userDetails = apiUserService.loadUserByUsername(adm.getName());
            if (security.pbkdf2Matches(adm.getPassword(), userDetails.getPassword())) {
                authenticate = authenticate(userDetails.getUsername(), userDetails.getPassword(), request);
            }
        } else {
            final String token = security.getAuthHeader(request);
            final String addr = request.getRemoteAddr();

            // Block ips here
            if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(addr)) {
                filterChain.doFilter(request, response);
                return;
            }

            try {
                String username = security.extractUsername(token);
                AuthenticatedUser userDetails = apiUserService.loadUserByUsername(username);
                authenticate = passwordVer(userDetails, request);
            } catch (ExpiredJwtException expt) {
                try {
                    AuthenticatedUser userDetails = apiUserService.loadUserByUsername(expt.getClaims().getSubject());
                    authenticate = passwordVer(userDetails, request);
                } catch (EmptyResultDataAccessException ex) {
                    authenticate = createWorkflow(addr, token, request);
                }
            } catch (EmptyResultDataAccessException | MalformedJwtException empt) {
                authenticate = createWorkflow(addr, token, request);
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        response.setHeader(security.getAuthHeader(),
                security.getTokenPrefix() + " " + security.createToken(authenticate));

        filterChain.doFilter(request, response);
    }
}
