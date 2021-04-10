package com.clickbait.plugin.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clickbait.plugin.CCUserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final String authentication;
    private final String tokenPrefix;
    private final String authHeader;
    private final CCUserDetailsService userDetailsService;
    private final JwtHandlers jwtTokenUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getMethod().equalsIgnoreCase("POST") || !request.getRequestURI().equals(authentication);
    }

    public AuthenticationFilter(JwtHandlers jwtTokenUtil, CCUserDetailsService userDetailsService, String tokenPrefix,
            String authHeader, String authentication) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenPrefix = tokenPrefix;
        this.authHeader = authHeader;
        this.authentication = authentication;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // DAO READY
        final String authorizationHeader = request.getHeader(authHeader);
        final String ip = request.getRemoteAddr();

        final UserDetails userDetails = userDetailsService.loadUserByUsername("foo");

        String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());

        response.setHeader(authHeader, tokenPrefix + " " + jwt);

        UsernamePasswordAuthenticationToken uPassAuthToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(uPassAuthToken);

        filterChain.doFilter(request, response);
    }
}
