package com.clickbait.plugin.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import com.clickbait.plugin.dao.Link;
import com.clickbait.plugin.dao.UserClick;
import com.clickbait.plugin.dao.UserTab;
import com.clickbait.plugin.security.AuthenticatedUser;
import com.clickbait.plugin.services.ApplicationDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('CLICKS_WRITE')")
    @PostMapping(value = "${api.clicks_register}")
    @ResponseStatus(value = HttpStatus.OK)
    public void registerLink(@Valid @RequestBody UserClick click, @AuthenticationPrincipal AuthenticatedUser user) {

        String domainName = dataService.extractHostname(click.getDomain());
        // Tflow external Tomcat here
        dataService.addClick(user.getUserId(), domainName, click.getLink(), 0.01f);
        //
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
            @AuthenticationPrincipal AuthenticatedUser user) {
        UUID userId = user.getUserId();

        List<Link> returnLinks = new ArrayList<Link>();
        final int tabId = requestTab.getTabId();
        final List<Link> links = requestTab.getLinks();
        String domainName = dataService.extractHostname(requestTab.getName());

        UserTab storedTab = dataService.getUserTab(userId, tabId);
        if (storedTab == null)
            dataService.insertTab(userId, domainName, tabId);

        final List<Link> storedLinks = storedTab.getLinks() != null ? storedTab.getLinks() : new ArrayList<Link>();
        returnLinks = storedLinks;

        if (links != null && !links.isEmpty()) {
            final List<Link> newLinks = links.stream()
                    .filter(x -> !storedLinks.stream().anyMatch(u -> u.getName().equals(x.getName())))
                    .collect(Collectors.toList());

            if (!newLinks.isEmpty()) {

                // Tflow external Tomcat here
                Random r = new Random();
                final List<Link> scoredLinks = newLinks.stream().map(a -> new Link(a.getName(), r.nextFloat()))
                        .collect(Collectors.toList());
                dataService.createPageModel(domainName, scoredLinks);
                //

                returnLinks = Stream.concat(scoredLinks.stream(), storedLinks.stream())
                        .sorted(Comparator.comparing(Link::getScore)).collect(Collectors.toList());
            }
        }

        return returnLinks;
    }
}
