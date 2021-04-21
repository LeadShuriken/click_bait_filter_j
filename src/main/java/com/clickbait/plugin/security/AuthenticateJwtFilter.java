package com.clickbait.plugin.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import com.google.common.base.Strings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clickbait.plugin.dao.User;
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

    private final ApplicationUserService apiUserService;
    private final EncryptionHandlers security;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return isAuthenticated();
    }

    public AuthenticateJwtFilter(ApplicationUserService apiUserService, EncryptionHandlers security) {
        this.apiUserService = apiUserService;
        this.security = security;
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    private void authenticate(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken uPassAuthToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(uPassAuthToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String token = security.getAuthHeader(request);
        final String addr = request.getRemoteAddr();

        if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(addr)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = security.extractUsername(token);
            if (security.validateToken(token, username)) {
                AuthenticatedUser userDetails = apiUserService.loadUserByUsername(username);
                authenticate(userDetails, request);
            }
        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }

        filterChain.doFilter(request, response);
    }
}
