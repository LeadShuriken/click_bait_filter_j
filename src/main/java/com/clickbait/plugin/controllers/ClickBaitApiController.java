package com.clickbait.plugin.controllers;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.clickbait.plugin.dao.UserClick;
import com.clickbait.plugin.dao.UserTab;
import com.clickbait.plugin.security.AuthenticatedUser;
import com.clickbait.plugin.services.ApplicationDataService;
import com.google.common.base.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.version}")
public class ClickBaitApiController {

    @Autowired
    private ApplicationDataService applicationDataService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('CLICKS_WRITE')")
    @PostMapping(value = "${api.clicks_register}")
    public String registerLink(@Valid @RequestBody UserClick click) {
        // 1. Get domain from body.domain by extractiong hostname
        // 2. Fetch all linkst for domain
        // 3. If by some freak accident there is no such domain aka land second
        // Register with name, link score (tflow)
        // 4. Else register click score (tflow) or increment and rescore
        // 5. Insert in clicks
        AuthenticatedUser user = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal());
        return "";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('DOMAINS_READ')")
    @PostMapping(value = "${api.page_segmentation}")
    public Map<String, Float> fetchPageSegmentation(@Valid @RequestBody UserTab tab) throws URISyntaxException {
        AuthenticatedUser user = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal());

        UserTab domainTab = null;
        String domain = tab.getName();
        if (!Strings.isNullOrEmpty(domain)) {
            domainTab = applicationDataService.getUserTab(user.getUserId(), tab.getTabId());
            if (domainTab == null) {
                domain = applicationDataService.extractHostname(domain);
                applicationDataService.insertTab(user.getUserId(), domain, tab.getTabId());
                domainTab = applicationDataService.getUserTab(user.getUserId(), tab.getTabId());
            }
        } else {
            domainTab = applicationDataService.getUserTab(user.getUserId(), tab.getTabId());
        }

        Map<String, Float> links = tab.getLinks();
        Map<String, Float> currentLinks = domainTab.getLinks();
        if (links != null) {
            if (currentLinks != null) {
                currentLinks = currentLinks.entrySet().stream().filter(x -> !links.containsKey(x.getKey()))
                        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            } else {
                currentLinks = null;
            }

            if (currentLinks != null) {
                Random r = new Random();
                currentLinks.entrySet().stream().map(x -> x.setValue(r.nextFloat()));
            }
        }

        return currentLinks;
    }
}
