package com.clickbait.plugin.controllers;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private ApplicationDataService dataService;

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
    public Map<String, Float> fetchPageSegmentation(@Valid @RequestBody UserTab requestTab) throws URISyntaxException {
        UUID userId = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserId();

        // Body bootstrap
        final int tabId = requestTab.getTabId();
        final Map<String, String[]> links = requestTab.getLinksData();
        String domainName = requestTab.getName();
        UserTab storedTab = null;

        if (!Strings.isNullOrEmpty(domainName)) {
            domainName = dataService.extractHostname(domainName);
            storedTab = dataService.getUserTab(userId, tabId);
            if (storedTab == null) {
                // No such tab, register but remains empty
                dataService.insertTab(userId, domainName, tabId);
            }
        } else {
            storedTab = dataService.getUserTab(userId, tabId);
            if (storedTab == null)
                return null;
            domainName = storedTab.getName();
        }

        final Map<String, Float> storedLinks = storedTab != null && storedTab.getLinks() != null ? storedTab.getLinks()
                : new HashMap<String, Float>();
        Map<String, Float> returnLinks = storedLinks;

        if (links != null && !links.isEmpty()) {
            final Map<String, String[]> newLinks = links.entrySet().stream()
                    .filter(x -> !storedLinks.containsKey(x.getKey()))
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

            if (!newLinks.isEmpty()) {

                // Tflow external Tomcat here
                Random r = new Random();
                final Map<String, Float> scoredLinks = newLinks.entrySet().stream()
                        .collect(Collectors.toMap(x -> x.getKey(), x -> r.nextFloat()));
                dataService.createPageModel(domainName, scoredLinks);
                //

                returnLinks = Stream.concat(scoredLinks.entrySet().stream(), storedLinks.entrySet().stream())
                        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            }
        }
        return returnLinks;
    }
}
