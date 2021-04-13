package com.clickbait.plugin.dao;

import java.util.UUID;

import javax.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTab {

    @JsonProperty("tabId")
    private final UUID tabId;

    @Min(1)
    @JsonProperty("index")
    private final int index;

    @JsonProperty("domainId")
    private final UUID domainId;

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
