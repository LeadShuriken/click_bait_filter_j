package com.clickbait.plugin.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clickbait.plugin.services.CustomUserDetailsService;

import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class AuthenticateJwtFilter extends OncePerRequestFilter {

    private final String processing;
    private final CustomUserDetailsService userDetailsService;
    private final EncryptionHandlers encryptionHandlers;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getMethod().equalsIgnoreCase("POST") || !request.getRequestURI().matches(processing)
                || isAuthenticated();
    }

    public AuthenticateJwtFilter(EncryptionHandlers encryptionHandlers, CustomUserDetailsService userDetailsService,
            String processing) {
        this.encryptionHandlers = encryptionHandlers;
        this.userDetailsService = userDetailsService;
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

        final String jwt = encryptionHandlers.getAuthHeader(request);
        final String username = encryptionHandlers.extractUsername(jwt);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        if (encryptionHandlers.validateToken(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken uPassAuthToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(uPassAuthToken);
        }

        filterChain.doFilter(request, response);
    }
}
