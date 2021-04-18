package com.clickbait.plugin.security;

import com.google.common.collect.Sets;

import org.springframework.security.core.GrantedAuthority;
import static com.clickbait.plugin.security.ApplicationUserPrivilege.*;

import java.util.Set;

public enum ApplicationUserRole implements GrantedAuthority {
    USER(Sets.newHashSet(
        USERS_WRITE,
        CLICKS_WRITE,
        DOMAINS_READ,
        DOMAINS_WRITE
    )),
    ADMIN(Sets.newHashSet(
        USERS_READ,
        USERS_WRITE,
        CLICKS_READ,
        CLICKS_WRITE,
        DOMAINS_READ,
        DOMAINS_WRITE
    ));

    private final Set<ApplicationUserPrivilege> privileges;

    ApplicationUserRole(Set<ApplicationUserPrivilege> privileges) {
        this.privileges = privileges;
    }

    public Set<ApplicationUserPrivilege> getPrivileges() {
        return privileges;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}