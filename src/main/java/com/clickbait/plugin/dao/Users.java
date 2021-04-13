package com.clickbait.plugin.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

import java.util.UUID;

public class Users {

    @JsonProperty("userId")
    private final UUID userId;

    @NotBlank
    @JsonProperty("name")
    private final String name;

    @NotBlank
    @JsonProperty("password")
    private final String password;

    public Users(UUID userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
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

    @Override
    public String toString() {
        return "User{userId=" + userId + ", name=" + name + ", password=" + password + "}";
    }
}
