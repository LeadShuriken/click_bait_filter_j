package com.clickbait.plugin.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component("exceptionHadlerFilter")
public class ExceptionHadlerFilter extends ClickBaitPluginFilter {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHadlerFilter.class);

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException ex) {
            logger.error(ex.getClass().getName(), ex);
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}