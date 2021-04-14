package com.clickbait.plugin.dao;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Privilege {

    @JsonProperty("privilegeId")
    private final UUID privilegeId;

    @NotBlank
    @JsonProperty("name")
    private final String name;

    public Privilege(UUID privilegeId, String name) {
        this.privilegeId = privilegeId;
        this.name = name;
    }
}
