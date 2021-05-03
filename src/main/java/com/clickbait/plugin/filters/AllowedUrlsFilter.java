package com.clickbait.plugin.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component("allowedUrlsFilter")
public class AllowedUrlsFilter extends ClickBaitPluginFilter {

    @Value("${api.allowedEndpoints}")
    private String allowed;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!request.getRequestURI().matches(allowed) || !request.getMethod().equalsIgnoreCase("POST")) {
            throw new RuntimeException("Endpoint not allowed");
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
