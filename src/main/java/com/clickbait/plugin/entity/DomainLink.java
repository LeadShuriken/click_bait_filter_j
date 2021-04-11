package com.clickbait.plugin.entity;

import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class DomainLink {

    @NotNull
    private final UUID domainId;

    @NotNull
    private final UUID linkId;

    @Min(1)
    private final long count;

    public DomainLink(UUID domainId, UUID linkId, Long count) {
        this.domainId = domainId;
        this.linkId = linkId;
        this.count = count;
    }

    public UUID getDomainId() {
        return domainId;
    }

    public UUID getLinkId() {
        return linkId;
    }

    public long getCount() {
        return count;
    }
}
