package com.clickbait.plugin.repository;

import com.clickbait.plugin.dao.*;
import com.clickbait.plugin.security.ApplicationUserPrivilege;
import com.clickbait.plugin.security.ApplicationUserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationDao {

    private final UserDataService userAccessService;
    private final TabDataService tabAccessService;
    private final ClickDataService clickAccessService;

    @Autowired
    public ApplicationDao(UserDataService userAccessService, TabDataService tabAccessService,
            ClickDataService clickAccessService) {
        this.clickAccessService = clickAccessService;
        this.userAccessService = userAccessService;
        this.tabAccessService = tabAccessService;
    }

    public List<User> getAllUsers() {
        return userAccessService.getAllUsers();
    }

    public User getUser(UUID id) {
        return userAccessService.getUser(id, null, null);
    }

    public User getUser(String name) {
        return userAccessService.getUser(null, name, null);
    }

    public User getUser(String name, String password) {
        return userAccessService.getUser(null, name, password);
    }

    public UUID addNewUser(String name, String password, ApplicationUserRole role) {
        if (userAccessService.isPasswordTaken(password)) {
            return null;
        }
        return userAccessService.insertUser(name, password, role.name());
    }

    public int deleteUser(UUID userId) {
        return userAccessService.deleteUser(userId);
    }

    public int updateUser(UUID userId, User user) {
        Optional.ofNullable(user.getPassword()).ifPresent(password -> {
            boolean taken = userAccessService.isPasswordTaken(password);
            if (!taken) {
                userAccessService.updatePassword(userId, password);
            } else {
                throw new IllegalStateException("Password is taken");
            }
        });

        String[] priv = null;
        if (user.getPrivileges() != null) {
            priv = user.getPrivileges().stream().map(ApplicationUserPrivilege::getPrivilege).toArray(String[]::new);
        }
        return userAccessService.updateUser(userId, user.getName(), user.getPassword(), user.getRole().name(), priv);
    }

    public int addPrivilige(UUID userId, List<ApplicationUserPrivilege> privileges) {
        return userAccessService.addPrivilige(userId,
                privileges.stream().map(ApplicationUserPrivilege::getPrivilege).toArray(String[]::new));
    }

    public int removePrivilige(UUID userId, List<ApplicationUserPrivilege> privileges) {
        return userAccessService.removePrivilige(userId,
                privileges.stream().map(ApplicationUserPrivilege::getPrivilege).toArray(String[]::new));
    }

    public List<UserTab> getAllTabs() {
        return tabAccessService.getAllTabs();
    }

    public List<UserTab> getUserTabs(UUID userId) {
        return tabAccessService.getAllTabs();
    }

    public UserTab getUserTab(UUID userId, int index) {
        return tabAccessService.getUserTab(userId, index);
    }

    public UUID insertTab(UUID userId, String name, int index) {
        return tabAccessService.insertTab(userId, name, index);
    }

    public List<UserClick> getAllClicks() {
        return clickAccessService.getAllClicks();
    }

    public UUID addClick(UUID userId, String domain, String link) {
        return clickAccessService.addClick(userId, domain, link);
    }

    public int activateUser(UUID userId, Boolean enabled, Boolean accountExpired, Boolean accountLocked,
            Boolean credExpired) {
        return userAccessService.activateUser(userId, enabled, accountExpired, accountLocked, credExpired);
    }

    public Boolean isUserActive(UUID userId) {
        return userAccessService.isUserActive(userId);
    }
}
