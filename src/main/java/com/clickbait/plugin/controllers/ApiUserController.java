package com.clickbait.plugin.controllers;

import com.clickbait.plugin.repository.ApplicationDataService;
import com.clickbait.plugin.security.ApplicationUserRole;
import com.clickbait.plugin.security.EncryptionHandlers;

import java.util.List;

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
public class ApiUserController {

    @Autowired
    private ApplicationDataService applicationDataService;

    @Autowired
    private EncryptionHandlers security;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "${api.endpoints.login_admin}")
    public void loginAdmin() {
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_READ')")
    @PostMapping(value = "${api.endpoints.is_active}")
    public boolean isActive(@Valid @RequestBody User user) {
        return applicationDataService.isUserActive(user.getUserId());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_READ')")
    @PostMapping(value = "${api.endpoints.set_active}")
    public void isSetActive(@Valid @RequestBody User user) {
        applicationDataService.activateUser(user.getUserId(), user.getEnabled(), user.getAccountExpired(),
                user.getAccountLocked(), user.getCredExpired());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_WRITE')")
    @PostMapping(value = "${api.endpoints.register_admin}")
    public User registerAdmin(@Valid @RequestBody User user) {
        return applicationDataService.getUser(applicationDataService.addNewUser(user.getName(),
                security.pbkdf2Hash(user.getPassword()), ApplicationUserRole.ADMIN));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_WRITE')")
    @PostMapping(value = "${api.endpoints.delete_user}")
    public void deleteUser(@Valid @RequestBody User user) {
        applicationDataService.deleteUser(user.getUserId());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_WRITE')")
    @PostMapping(value = "${api.endpoints.update_user}")
    public void updateUser(@Valid @RequestBody User user) {
        applicationDataService.updateUser(user.getUserId(), user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_READ')")
    @PostMapping(value = "${api.endpoints.get_user}")
    public User getUserByUsernamePassword(@Valid @RequestBody User user) {
        if (user.getUserId() != null) {
            return applicationDataService.getUser(user.getUserId());
        }
        return applicationDataService.getUser(user.getName(), user.getPassword());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('USERS_READ')")
    @PostMapping(value = "${api.endpoints.get_all_user}")
    public List<User> getUsers() {
        return applicationDataService.getAllUsers();
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
}
