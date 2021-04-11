package com.clickbait.plugin.entity;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserClick {

    @NotNull
    @JsonProperty("userId")
    private final UUID userId;

    @NotNull
    @JsonProperty("clickId")
    private final UUID clickId;

    @NotNull
    @JsonProperty("domainId")
    private final UUID domainId;

    @NotNull
    @JsonProperty("linkId")
    private final UUID linkId;

    @Past
    @JsonProperty("atTime")
    private final LocalDate atTime;

    public UserClick(UUID userId, UUID clickId, UUID domainId, UUID linkId, LocalDate atTime) {
        this.userId = userId;
        this.clickId = clickId;
        this.domainId = domainId;
        this.linkId = linkId;
        this.atTime = atTime;
    }
}
