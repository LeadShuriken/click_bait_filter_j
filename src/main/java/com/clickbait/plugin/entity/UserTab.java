package com.clickbait.plugin.entity;

import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserTab {

    @NotNull
    private final UUID userId;

    @NotNull
    private final UUID tabId;

    @NotNull
    private final UUID domainId;

    @Min(1)
    private final int index;

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
