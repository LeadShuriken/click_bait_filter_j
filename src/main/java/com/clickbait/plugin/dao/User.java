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

    public User(UUID userId) {
        this.userId = userId;
        this.name = null;
        this.password = null;
        this.privileges = null;
        this.role = null;
    }

    public User(String name, String password) {
        this.userId = null;
        this.name = name;
        this.password = password;
        this.privileges = null;
        this.role = null;
    }

    public User(String name, String password, ApplicationUserRole role) {
        this.userId = null;
        this.name = name;
        this.password = password;
        this.privileges = null;
        this.role = role;
    }

    public User(String name, String password, ApplicationUserRole role, List<ApplicationUserPrivilege> privileges) {
        this.userId = null;
        this.name = name;
        this.password = password;
        this.privileges = privileges;
        this.role = role;
    }

    public User(UUID userId, String name, String password, ApplicationUserRole role,
            List<ApplicationUserPrivilege> privileges) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.privileges = privileges;
        this.role = role;
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

    public String getRole() {
        return role != null ? role.getAuthority() : null;
    }

    @Override
    public String toString() {
        return "User{name=" + name + ", password=" + password + ", role=" + role.getAuthority() + ", privileges="
                + privileges + "}";
    }
}
