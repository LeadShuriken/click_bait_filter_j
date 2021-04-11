package com.clickbait.plugin.entity;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Domain {

    @NotNull
    @JsonProperty("domainId")
    private final UUID domainId;

    @NotBlank
    @JsonProperty("name")
    private final String name;

    public Domain(UUID domainId, String name) {
        this.domainId = domainId;
        this.name = name;
    }

    public UUID getDomainId() {
        return domainId;
    }

    public String getName() {
        return name;
    }
}
