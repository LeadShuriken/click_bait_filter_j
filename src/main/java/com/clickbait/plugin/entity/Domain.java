package com.clickbait.plugin.entity;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Domain {

    @NotNull
    private final UUID domainId;

    @NotBlank
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
