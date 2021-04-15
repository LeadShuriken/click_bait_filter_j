package com.clickbait.plugin.dao;

import java.util.UUID;

import javax.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTab {

    @Min(1)
    @JsonProperty("index")
    private final int index;

    @JsonProperty("domainId")
    private final UUID domainId;

    @JsonProperty("userId")
    private final UUID userId;

    @JsonProperty("name")
    private final String name;

    public UserTab(UUID userId, UUID domainId, int index, String name) {
        this.userId = userId;
        this.domainId = domainId;
        this.index = index;
        this.name = name;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getDomainId() {
        return domainId;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UserTab{index=" + index + ", name=" + name + "}";
    }
}
