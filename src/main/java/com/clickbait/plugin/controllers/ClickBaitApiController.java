package com.clickbait.plugin.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.clickbait.plugin.dao.Link;
import com.clickbait.plugin.dao.UserTab;
import com.clickbait.plugin.dao.UserClick;
import org.springframework.http.HttpStatus;

import com.clickbait.plugin.security.AuthenticatedUser;
import com.clickbait.plugin.services.ApplicationDataService;
import com.clickbait.plugin.thirdPartyRest.TFlowRestService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.version}")
public class ClickBaitApiController {

    @Autowired
    private ApplicationDataService dataService;

    @Autowired
    private TFlowRestService tflowRest;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('CLICKS_WRITE')")
    @PostMapping(value = "${api.clicks_register}")
    @ResponseStatus(value = HttpStatus.OK)
    public void registerLink(@Valid @RequestBody UserClick click, @AuthenticationPrincipal AuthenticatedUser user,
            HttpServletRequest request) {
        final String domainName = dataService.extractHostname(click.getDomain());
        final Link scored = tflowRest.getScore(request, click.getLink());
        dataService.addClick(user.getUserId(), domainName, scored.getName(), scored.getScore());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('DOMAINS_READ')")
    @PostMapping(value = "${api.page_tab_segmentation}")
    public @ResponseBody List<Link> pageTabSegmentation(@Valid @RequestBody UserTab requestTab,
            @AuthenticationPrincipal AuthenticatedUser user) {
        UserTab storedTab = dataService.getUserTab(user.getUserId(), requestTab.getTabId());
        if (storedTab == null)
            return new ArrayList<Link>();

        return storedTab.getLinks() != null ? storedTab.getLinks() : new ArrayList<Link>();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('DOMAINS_READ')")
    @PostMapping(value = "${api.page_segmentation}")
    public @ResponseBody List<Link> fetchPageSegmentation(@Valid @RequestBody UserTab requestTab,
            @AuthenticationPrincipal AuthenticatedUser user, HttpServletRequest request) {
        UUID userId = user.getUserId();

        List<Link> returnLinks = new ArrayList<Link>();
        final int tabId = requestTab.getTabId();
        final List<Link> links = requestTab.getLinks();
        String domainName = dataService.extractHostname(requestTab.getName());

        UserTab storedTab = dataService.getUserTab(userId, tabId);

        List<Link> tempLinks = new ArrayList<Link>();
        if (storedTab == null) {
            dataService.insertTab(userId, domainName, tabId);
        } else {
            tempLinks = storedTab.getLinks() != null ? storedTab.getLinks() : new ArrayList<Link>();
        }

        final List<Link> storedLinks = tempLinks;
        returnLinks = tempLinks;

        if (links != null && !links.isEmpty()) {
            final List<Link> newLinks = links.stream()
                    .filter(x -> !storedLinks.stream().anyMatch(u -> u.getName().equals(x.getName())))
                    .collect(Collectors.toList());

            if (!newLinks.isEmpty()) {
                final List<Link> scoredLinks = tflowRest.getScores(request, new UserTab(newLinks));
                dataService.createPageModel(domainName, scoredLinks);

                returnLinks = Stream.concat(scoredLinks.stream(), storedLinks.stream())
                        .sorted(Comparator.comparing(Link::getScore)).collect(Collectors.toList());
            }
        }

        return returnLinks;
    }
}
