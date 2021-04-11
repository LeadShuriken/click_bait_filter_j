package com.clickbait.plugin.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class Users {

    @NotNull
    private final UUID userId;

    @NotBlank
    private final String name;

    @NotBlank
    private final String password;

    public Users(@JsonProperty("userId") UUID userId, @JsonProperty("name") String name,
            @JsonProperty("password") String password) {
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
}
