package com.clickbait.plugin.services;

import com.clickbait.plugin.dao.*;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserAccessService userAccessService;

    @Autowired
    public UserService(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }

    public List<User> getAllUsers() {
        return userAccessService.getAllUsers();
    }

    public User getUser(String name, String password) {
        return userAccessService.getUser(name, password);
    }

    public void addNewUser(User user) {
        if (userAccessService.isPasswordTaken(user.getPassword())) {
            throw new IllegalStateException("Password is taken");
        }

        userAccessService.insertUser(user.getName(), user.getPassword(), user.getRole());
    }

    public void deleteUser(UUID userId) {
        userAccessService.deleteUser(userId);
    }

    public void updateUser(UUID userId, User user) {
        Optional.ofNullable(user.getPassword()).ifPresent(password -> {
            boolean taken = userAccessService.isPasswordTaken(password);
            if (!taken) {
                userAccessService.updatePassword(userId, password);
            } else {
                throw new IllegalStateException("Password is taken");
            }
        });

        // Move in procedure
        Optional.ofNullable(user.getName()).filter(name -> Strings.isNotEmpty(name))
                .ifPresent(name -> userAccessService.updateName(userId, name));

        Optional.ofNullable(user.getPassword()).filter(password -> Strings.isNotEmpty(password))
                .ifPresent(password -> userAccessService.updatePassword(userId, password));

        Optional.ofNullable(user.getRole()).filter(role -> Strings.isNotEmpty(role))
                .ifPresent(role -> userAccessService.updateRole(userId, role));
    }
}
