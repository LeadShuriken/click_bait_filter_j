package com.clickbait.plugin.services;

import com.clickbait.plugin.annotations.SqlExceptionPass;
import com.clickbait.plugin.dao.*;
import com.clickbait.plugin.security.ApplicationUserPrivilege;
import com.clickbait.plugin.security.ApplicationUserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicationDataService {

    private final UsersDataService userDataService;
    private final TabDataService tabDataService;
    private final ClickDataService clickDataService;

    @Autowired
    public ApplicationDataService(UsersDataService userDataService, TabDataService tabDataService,
            ClickDataService clickDataService) {
        this.clickDataService = clickDataService;
        this.userDataService = userDataService;
        this.tabDataService = tabDataService;
    }

    public String extractHostname(String url) throws URISyntaxException {
        String domain = new URI(url).getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public List<User> getAllUsers() {
        return userDataService.getAllUsers();
    }

    public User getUser(UUID id) {
        return userDataService.getUser(id, null, null);
    }

    public User getUser(String name) {
        return userDataService.getUser(null, name, null);
    }

    public User getUser(String name, String password) {
        return userDataService.getUser(null, name, password);
    }

    public UUID addNewUser(String name, String password, ApplicationUserRole role) {
        if (userDataService.isPasswordTaken(password)) {
            return null;
        }
        return userDataService.insertUser(name, password, role.name());
    }

    public int deleteUser(UUID userId) {
        return userDataService.deleteUser(userId);
    }

    public int updateUser(UUID userId, User user) {
        Optional.ofNullable(user.getPassword()).ifPresent(password -> {
            boolean taken = userDataService.isPasswordTaken(password);
            if (!taken) {
                userDataService.updatePassword(userId, password);
            } else {
                throw new IllegalStateException("Password is taken");
            }
        });

        String[] priv = null;
        if (user.getPrivileges() != null) {
            priv = user.getPrivileges().stream().map(ApplicationUserPrivilege::getPrivilege).toArray(String[]::new);
        }
        return userDataService.updateUser(userId, user.getName(), user.getPassword(), user.getRole().name(), priv);
    }

    public int addPrivilige(UUID userId, List<ApplicationUserPrivilege> privileges) {
        return userDataService.addPrivilige(userId,
                privileges.stream().map(ApplicationUserPrivilege::getPrivilege).toArray(String[]::new));
    }

    public int removePrivilige(UUID userId, List<ApplicationUserPrivilege> privileges) {
        return userDataService.removePrivilige(userId,
                privileges.stream().map(ApplicationUserPrivilege::getPrivilege).toArray(String[]::new));
    }

    public List<UserTab> getAllTabs() {
        return tabDataService.getAllTabs();
    }

    public List<UserTab> getUserTabs(UUID userId) {
        return tabDataService.getUserTabs(userId);
    }

    @SqlExceptionPass
    public UserTab getUserTab(UUID userId, int index) {
        return tabDataService.getUserTab(userId, index);
    }

    public UUID insertTab(UUID userId, String name, int index) {
        return tabDataService.insertTab(userId, name, index);
    }

    public List<UserClick> getDomainClicks(UUID userId, String name) {
        return clickDataService.getDomainClicks(userId, name);
    }

    public UUID addClick(UUID userId, String domain, String link, Float baitScore) {
        return clickDataService.addClick(userId, domain, link, baitScore);
    }

    public int activateUser(UUID userId, Boolean enabled, Boolean accountExpired, Boolean accountLocked,
            Boolean credExpired) {
        return userDataService.activateUser(userId, enabled, accountExpired, accountLocked, credExpired);
    }

    public int setTflowToken(UUID userId, String token) {
        return userDataService.setTflowToken(userId, token);
    }
}
