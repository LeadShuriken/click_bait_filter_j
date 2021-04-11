package com.clickbait.plugin.entity;

import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DomainLink {

    @NotNull
    @JsonProperty("linkId")
    private final UUID linkId;

    @Min(1)
    @JsonProperty("count")
    private final long count;

    @NotBlank
    @JsonProperty("link")
    private final String link;

    @NotNull
    @JsonProperty("domainId")
    private final UUID domainId;

    public DomainLink(UUID domainId, UUID linkId, Long count, String link) {
        this.domainId = domainId;
        this.linkId = linkId;
        this.count = count;
        this.link = link;
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
