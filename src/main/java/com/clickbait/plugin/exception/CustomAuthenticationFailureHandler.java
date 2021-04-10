package com.clickbait.plugin.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
// import org.springframework.web.bind.annotation.ControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// @ControllerAdvice
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    // Not about to when auth with OncePerRequest Filters
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException ex) throws IOException, ServletException {
        logger.error(ex.getClass().getName(), ex);
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}