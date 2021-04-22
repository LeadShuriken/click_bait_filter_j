package com.clickbait.plugin.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.clickbait.plugin.dao.User;
import com.clickbait.plugin.dao.UserClick;
import com.clickbait.plugin.dao.UserTab;
import com.clickbait.plugin.repository.ApplicationDataService;
import com.clickbait.plugin.security.AuthenticatedUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.version}")
public class ApiTabsController {

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

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('CLICKS_READ')")
    @PostMapping(value = "${api.get_clicks}")
    public List<UserClick> getUserClicks(@Valid @RequestBody UserTab tab) {
        return applicationDataService.getDomainClicks(tab.getUserId(), tab.getName());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DOMAINS_WRITE')")
    @PostMapping(value = "${api.insert_tab}")
    public void insertUserTab(@Valid @RequestBody UserTab tab) {
        applicationDataService.insertTab(tab.getUserId(), tab.getName(), tab.getIndex());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('CLICKS_WRITE')")
    @PostMapping(value = "${api.insert_click}")
    public void insertClick(@Valid @RequestBody UserClick click) {
        applicationDataService.addClick(click.getUserId(), click.getDomain(), click.getLink());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DOMAINS_READ')")
    @PostMapping(value = "${api.get_all_tabs}")
    public List<UserTab> getAllTabs() {
        return applicationDataService.getAllTabs();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DOMAINS_READ')")
    @PostMapping(value = "${api.get_user_tabs}")
    public List<UserTab> getUserTabs(@Valid @RequestBody User user) {
        return applicationDataService.getUserTabs(user.getUserId());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DOMAINS_READ')")
    @PostMapping(value = "${api.get_user_tab}")
    public UserTab getUserTab(@Valid @RequestBody UserTab tab) {
        return applicationDataService.getUserTab(tab.getUserId(), tab.getIndex());
    }
}
