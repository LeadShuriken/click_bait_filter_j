package com.clickbait.plugin.dao;

import com.clickbait.plugin.security.ApplicationUserRole;
import com.clickbait.plugin.security.ApplicationUserPrivilege;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public class User {

    @JsonProperty("userId")
    private final UUID userId;

    @NotBlank
    @JsonProperty("name")
    private final String name;

    @NotBlank
    @JsonProperty("password")
    private final String password;

    @NotBlank
    @JsonProperty("role")
    private final ApplicationUserRole role;

    @NotBlank
    @JsonProperty("privileges")
    private final List<ApplicationUserPrivilege> privileges;

    @NotBlank
    @JsonProperty("enabled")
    private final Boolean enabled;

    @NotBlank
    @JsonProperty("accountExpired")
    private final Boolean accountExpired;

    @NotBlank
    @JsonProperty("accountLocked")
    private final Boolean accountLocked;

    @NotBlank
    @JsonProperty("credExpired")
    private final Boolean credExpired;

    public User(UUID userId) {
        this.userId = userId;
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

    public User(String name, String password, ApplicationUserRole role) {
        this.userId = null;
        this.name = name;
        this.password = password;
        this.privileges = null;
        this.role = role;
        this.enabled = null;
        this.accountExpired = null;
        this.accountLocked = null;
        this.credExpired = null;
    }

    public User(String name, String password, ApplicationUserRole role, List<ApplicationUserPrivilege> privileges) {
        this.userId = null;
        this.name = name;
        this.password = password;
        this.privileges = privileges;
        this.role = role;
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
        return "User{name=" + name + ", password=" + password + ", role=" + getRole().name() + ", privileges="
                + privileges + "}";
    }
}
