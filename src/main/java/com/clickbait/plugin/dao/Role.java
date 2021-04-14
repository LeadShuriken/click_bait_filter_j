package com.clickbait.plugin.dao;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {

    @JsonProperty("roleId")
    private final UUID roleId;

    @NotBlank
    @JsonProperty("name")
    private final String name;

    public Role(UUID roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }
}
