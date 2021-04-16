package com.clickbait.plugin.services;

import com.clickbait.plugin.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiService {

    private final UserDataService userAccessService;
    private final TabDataService tabAccessService;
    private final ClickDataService clickAccessService;

    @Autowired
    public ApiService(UserDataService userAccessService, TabDataService tabAccessService,
            ClickDataService clickAccessService) {
        this.clickAccessService = clickAccessService;
        this.userAccessService = userAccessService;
        this.tabAccessService = tabAccessService;
    }

    public List<User> getAllUsers() {
        return userAccessService.getAllUsers();
    }

    public User getUser(String name, String password) {
        return userAccessService.getUser(name, password);
    }

    public UUID addNewUser(User user) {
        if (userAccessService.isPasswordTaken(user.getPassword())) {
            return null;
        }
        return userAccessService.insertUser(user.getName(), user.getPassword(), user.getRole());
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

        return userAccessService.updateUser(userId, user.getName(), user.getPassword(), user.getRole());
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
        return this.clickAccessService.getAllClicks();
    }

    public UUID addClick(UUID userId, String domain, String link) {
        return this.clickAccessService.addClick(userId, domain, link);
    }
}
