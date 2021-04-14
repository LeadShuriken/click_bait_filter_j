package com.clickbait.plugin.dao;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Domain {

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
