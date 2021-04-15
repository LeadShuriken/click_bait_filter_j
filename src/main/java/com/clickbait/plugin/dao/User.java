package com.clickbait.plugin.dao;

import com.clickbait.plugin.security.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

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
    private final Role role;

    public User(UUID userId, String name, String password, Role role) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role.getAuthority();
    }

    @Override
    public String toString() {
        return "User{name=" + name + ", password=" + password + ", role=" + role.getAuthority() + "}";
    }
}
