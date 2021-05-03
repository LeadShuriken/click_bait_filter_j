package com.clickbait.plugin.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component("allowedOriginFilter")
public class ApiOriginFilter extends ClickBaitPluginFilter {

    @Value("${filters.allowedOrigin}")
    private String origin;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String localOrigin = request.getHeader("Origin");
        if (!Strings.isNullOrEmpty(localOrigin) && localOrigin.startsWith(origin)) {
            filterChain.doFilter(request, response);
        } else {
            throw new RuntimeException("Origin not allowed");
        }
    }
}
