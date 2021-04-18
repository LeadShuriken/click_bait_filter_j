package com.clickbait.plugin.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clickbait.plugin.services.CustomUserDetailsService;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final String authentication;
    private final CustomUserDetailsService userDetailsService;
    private final EncryptionHandlers encryptionHandlers;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getMethod().equalsIgnoreCase("POST") || !request.getRequestURI().equals(authentication);
    }

    public AuthenticationFilter(EncryptionHandlers encryptionHandlers, CustomUserDetailsService userDetailsService,
            String authentication) {
        this.userDetailsService = userDetailsService;
        this.encryptionHandlers = encryptionHandlers;
        this.authentication = authentication;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // final String username = encryptionHandlers.hash(request.getRemoteAddr(),
        // encryptionHandlers.getAuthHeader(request));
        final UserDetails userDetails = userDetailsService.loadUserByUsername("foo");

        String jwt = encryptionHandlers.generateToken(userDetails.getUsername());

        response.setHeader(encryptionHandlers.getAuthHeader(), encryptionHandlers.getTokenPrefix() + " " + jwt);

        UsernamePasswordAuthenticationToken uPassAuthToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(uPassAuthToken);

        filterChain.doFilter(request, response);
    }
}
