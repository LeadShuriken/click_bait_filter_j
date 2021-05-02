package com.clickbait.plugin.dao;

import com.clickbait.plugin.annotations.UUIDA;
import com.clickbait.plugin.annotations.SQLInjectionSafe;
import com.clickbait.plugin.security.ApplicationUserRole;
import com.clickbait.plugin.security.ApplicationUserPrivilege;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @UUIDA
    @JsonProperty("userId")
    private final UUID userId;

    @SQLInjectionSafe
    @JsonProperty("name")
    @Size(max = 100, min = 1)
    private final String name;

    @SQLInjectionSafe
    @JsonProperty("password")
    @Size(max = 200, min = 1)
    private final String password;

    @JsonProperty("role")
    private final ApplicationUserRole role;

    @JsonProperty("privileges")
    private final List<ApplicationUserPrivilege> privileges;

    @JsonProperty("enabled")
    private final Boolean enabled;

    @JsonProperty("accountExpired")
    private final Boolean accountExpired;

    @JsonProperty("accountLocked")
    private final Boolean accountLocked;

    @JsonProperty("credExpired")
    private final Boolean credExpired;

    public User() {
        this.userId = null;
        this.name = null;
        this.password = null;
        this.privileges = null;
        this.role = null;
        this.enabled = null;
        this.accountExpired = null;
        this.accountLocked = null;
        this.credExpired = null;
    }

    public User(String name, String password) {
        this.userId = null;
        this.name = name;
        this.password = password;
        this.privileges = null;
        this.role = null;
        this.enabled = null;
        this.accountExpired = null;
        this.accountLocked = null;
        this.credExpired = null;
    }

    public User(UUID userId, String name, String password, ApplicationUserRole role,
            List<ApplicationUserPrivilege> privileges, Boolean enabled, Boolean accountExpired, Boolean accountLocked,
            Boolean credExpired) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.privileges = privileges;
        this.role = role;
        this.enabled = enabled;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.credExpired = credExpired;
    }

    public User(UUID userId, String name, ApplicationUserRole role, List<ApplicationUserPrivilege> privileges) {
        this.userId = userId;
        this.name = name;
        this.privileges = privileges;
        this.role = role;
        this.enabled = null;
        this.password = null;
        this.accountExpired = null;
        this.accountLocked = null;
        this.credExpired = null;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean getAccountExpired() {
        return accountExpired;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public Boolean getCredExpired() {
        return credExpired;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<ApplicationUserPrivilege> getPrivileges() {
        return privileges;
    }

    public String getPassword() {
        return password;
    }

    public ApplicationUserRole getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{userId=" + userId + ",name=" + name + ", password=" + password + ", role=" + getRole().name()
                + ", privileges=" + privileges + "}";
    }
}
