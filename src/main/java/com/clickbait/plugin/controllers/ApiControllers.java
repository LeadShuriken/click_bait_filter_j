package com.clickbait.plugin.controllers;

import com.clickbait.plugin.repository.ApplicationDataService;
import com.clickbait.plugin.security.ApplicationUserRole;
import javax.validation.Valid;

import com.clickbait.plugin.dao.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${api.version}")
public class ApiControllers {

    @Autowired
    private ApplicationDataService applicationDataService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping(value = "${api.endpoints.register_admin}")
    public User registerAdmin(@Valid @RequestBody User user) {
        return applicationDataService.getUser(
                applicationDataService.addNewUser(user.getName(), user.getPassword(), ApplicationUserRole.ADMIN));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping(value = "${api.endpoints.clicks_register}")
    public String registerLink() {
        return "Hello Click!";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping(value = "${api.endpoints.page_segmentation}")
    public String fetchPageSegmentation() {
        return "Hello Page Segmentation!";
    }
}
