package com.clickbait.plugin.controllers;

import java.util.UUID;

import com.clickbait.plugin.repository.ApplicationDataService;
import com.clickbait.plugin.security.AuthenticatedUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.version}")
public class ClickBaitApiController {

    @Autowired
    private ApplicationDataService applicationDataService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('CLICKS_WRITE')")
    @PostMapping(value = "${api.clicks_register}")
    public String registerLink() {
        UUID a = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserId();
        return a.toString();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('DOMAINS_READ')")
    @PostMapping(value = "${api.page_segmentation}")
    public String fetchPageSegmentation() {
        return "Hello Page Segmentation!";
    }
}
