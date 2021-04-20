package com.clickbait.plugin.security;

import com.google.common.collect.Sets;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.clickbait.plugin.security.ApplicationUserPrivilege.*;

import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationUserRole {
    USER(Sets.newHashSet(CLICKS_WRITE, DOMAINS_READ)),
    ADMIN(Sets.newHashSet(USERS_READ, USERS_WRITE, CLICKS_READ, CLICKS_WRITE, DOMAINS_READ, DOMAINS_WRITE));

    private final Set<ApplicationUserPrivilege> privileges;

    ApplicationUserRole(Set<ApplicationUserPrivilege> privileges) {
        this.privileges = privileges;
    }

    public Set<ApplicationUserPrivilege> getPrivileges() {
        return privileges;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPrivileges().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPrivilege())).collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}