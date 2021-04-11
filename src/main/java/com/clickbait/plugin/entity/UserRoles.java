package com.clickbait.plugin.entity;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRoles {

    @NotNull
    @JsonProperty("userId")
    private final UUID userId;

    @NotNull
    @JsonProperty("roleId")
    private final UUID roleId;

    @NotBlank
    @JsonProperty("name")
    private final String name;

    public UserRoles(UUID userId, UUID roleId, String name) {
        this.userId = userId;
        this.roleId = roleId;
        this.name = name;
    }
}
