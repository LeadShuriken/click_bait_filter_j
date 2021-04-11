package com.clickbait.plugin.entity;

import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTab {

    @NotNull
    @JsonProperty("tabId")
    private final UUID tabId;

    @Min(1)
    @JsonProperty("index")
    private final int index;

    @NotNull
    @JsonProperty("domainId")
    private final UUID domainId;

    @NotNull
    @JsonProperty("userId")
    private final UUID userId;

    public UserTab(UUID userId, UUID tabId, UUID domainId, int index) {
        this.userId = userId;
        this.tabId = tabId;
        this.domainId = domainId;
        this.index = index;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getTabId() {
        return tabId;
    }

    public UUID getDomainId() {
        return domainId;
    }

    public int getIndex() {
        return index;
    }
}
