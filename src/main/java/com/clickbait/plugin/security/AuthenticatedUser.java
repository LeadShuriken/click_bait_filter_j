package com.clickbait.plugin.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import com.clickbait.plugin.dao.User;

public class AuthenticatedUser extends User implements UserDetails {

    private final User user;

    protected AuthenticatedUser(User user) {
        super(user.getName(), user.getPassword());
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getGrantedAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getCredExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }
}