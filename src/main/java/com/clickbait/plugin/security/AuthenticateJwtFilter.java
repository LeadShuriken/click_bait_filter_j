package com.clickbait.plugin.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import com.clickbait.plugin.CCUserDetailsService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class AuthenticateJwtFilter extends OncePerRequestFilter {

    private final String processing;
    private final String tokenPrefix;
    private final String authHeader;
    private final CCUserDetailsService userDetailsService;
    private final JwtHandlers jwtTokenUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getMethod().equalsIgnoreCase("POST") || !request.getRequestURI().matches(processing)
                || isAuthenticated();
    }

    public AuthenticateJwtFilter(JwtHandlers jwtTokenUtil, CCUserDetailsService userDetailsService, String tokenPrefix,
            String authHeader, String processing) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.tokenPrefix = tokenPrefix;
        this.authHeader = authHeader;
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

        final String authorizationHeader = request.getHeader(authHeader);
        final String jwt = authorizationHeader.substring(tokenPrefix.length() + 1);
        final String username = jwtTokenUtil.extractUsername(jwt);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken uPassAuthToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(uPassAuthToken);
        }

        filterChain.doFilter(request, response);
    }
}
