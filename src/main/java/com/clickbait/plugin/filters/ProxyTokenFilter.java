package com.clickbait.plugin.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clickbait.plugin.security.AuthenticatedUser;
import com.clickbait.plugin.security.EncryptionHandlers;
import com.clickbait.plugin.services.ApplicationDataService;

import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;

@Order(value = Ordered.LOWEST_PRECEDENCE)
@Component("proxyTokenFilter")
public class ProxyTokenFilter extends ClickBaitPluginFilter {

    @Autowired
    private ApplicationDataService appDataService;

    @Autowired
    private EncryptionHandlers enc;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        appDataService.setTflowToken(user.getUserId(), enc.pbkdf2Hash(user.getUsername(), enc.getTflowToken()));
        filterChain.doFilter(request, response);
        appDataService.setTflowToken(user.getUserId(), null);
    }
}