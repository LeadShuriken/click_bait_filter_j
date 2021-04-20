package com.clickbait.plugin.services;

import java.util.UUID;

import com.clickbait.plugin.dao.User;
import com.clickbait.plugin.repository.ApplicationDataService;
import com.clickbait.plugin.security.ApplicationUserRole;
import com.clickbait.plugin.security.AuthenticatedUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationDataService applicationDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthenticatedUser(applicationDao.getUser(username));
    }

    @Autowired
    public ApplicationUserService(ApplicationDataService userService) {
        this.applicationDao = userService;
    }

    public UserDetails loadOrCreateUserByUsernamePassword(String username, String password) {
        User a = applicationDao.getUser(username, password);
        if (a == null) {
            UUID newUserId = applicationDao.addNewUser(username, password, ApplicationUserRole.USER);
            a = applicationDao.getUser(newUserId);
        }
        return new AuthenticatedUser(a);
    }

    public UserDetails loadUserByUsernamePassword(String username, String password) {
        return new AuthenticatedUser(applicationDao.getUser(username, password));
    }
}
