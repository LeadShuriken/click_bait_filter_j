package com.clickbait.plugin.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Value;

public abstract class ClickBaitPluginFilter extends OncePerRequestFilter {

    @Value("${filters.activeFilters}")
    private List<String> activeFilters;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !activeFilters.contains(this.getFilterName());
    }

    @Override
    abstract protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException;
}
