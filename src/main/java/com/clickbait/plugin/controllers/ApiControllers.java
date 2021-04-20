package com.clickbait.plugin.controllers;

import com.clickbait.plugin.repository.ApplicationDataService;
import com.clickbait.plugin.security.ApplicationUserRole;

import javax.validation.Valid;

import com.clickbait.plugin.dao.User;
import com.clickbait.plugin.dao.PrivilegesRequest;

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

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_WRITE')")
    @PostMapping(value = "${api.endpoints.register_admin}")
    public User registerAdmin(@Valid @RequestBody User user) {
        return applicationDataService.getUser(
                applicationDataService.addNewUser(user.getName(), user.getPassword(), ApplicationUserRole.ADMIN));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_READ')")
    @PostMapping(value = "${api.endpoints.get_user}")
    public User getUserByUsernamePassword(@Valid @RequestBody User user) {
        if (user.getUserId() != null) {
            return applicationDataService.getUser(user.getUserId());
        }
        return applicationDataService.getUser(user.getName(), user.getPassword());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_WRITE')")
    @PostMapping(value = "${api.endpoints.add_priv}")
    public void addPrivilege(@Valid @RequestBody PrivilegesRequest privileges) {
        applicationDataService.addPrivilige(privileges.getUserId(), privileges.getPrivileges());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_WRITE')")
    @PostMapping(value = "${api.endpoints.remove_priv}")
    public void removePrivilege(@Valid @RequestBody PrivilegesRequest privileges) {
        applicationDataService.removePrivilige(privileges.getUserId(), privileges.getPrivileges());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('CLICKS_WRITE')")
    @PostMapping(value = "${api.endpoints.clicks_register}")
    public String registerLink() {
        return "Hello Click!";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER') and hasAuthority('DOMAINS_READ')")
    @PostMapping(value = "${api.endpoints.page_segmentation}")
    public String fetchPageSegmentation() {
        return "Hello Page Segmentation!";
    }
}
