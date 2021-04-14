package com.clickbait.plugin.dao;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRole {

    @JsonProperty("userId")
    private final UUID userId;

    @JsonProperty("roleId")
    private final UUID roleId;

    @NotBlank
    @JsonProperty("name")
    private final String name;

    public UserRole(UUID userId, UUID roleId, String name) {
        this.userId = userId;
        this.roleId = roleId;
        this.name = name;
    }
}
