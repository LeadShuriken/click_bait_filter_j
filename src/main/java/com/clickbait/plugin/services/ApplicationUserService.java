package com.clickbait.plugin.services;

import com.clickbait.plugin.dao.User;
import com.clickbait.plugin.security.ApplicationUserRole;
import com.clickbait.plugin.security.AuthenticatedUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final UsersDataService usersDataService;

    @Override
    public AuthenticatedUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthenticatedUser(usersDataService.getServiceUser(null, username, null));
    }

    @Autowired
    public ApplicationUserService(UsersDataService usersDataService) {
        this.usersDataService = usersDataService;
    }

    public UserDetails loadOrCreateUserByUsernamePassword(String username, String password) {
        User a = null;
        try {
            a = usersDataService.getServiceUser(null, username, password);
        } catch (EmptyResultDataAccessException e) {
            a = usersDataService.getServiceUser(
                    usersDataService.insertUser(username, password, ApplicationUserRole.USER.name()), null, null);
        }
        return new AuthenticatedUser(a);
    }

    public UserDetails loadUserByUsernamePassword(String username, String password) {
        return new AuthenticatedUser(usersDataService.getServiceUser(null, username, password));
    }
}
