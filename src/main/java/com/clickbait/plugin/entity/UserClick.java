package com.clickbait.plugin.entity;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

public class UserClick {

    @NotNull
    private final UUID userId;

    @NotNull
    private final UUID clickId;

    @NotNull
    private final UUID domainId;

    @NotNull
    private final UUID linkId;

    @Past
    private final LocalDate atTime;

    public UserClick(UUID userId, UUID clickId, UUID domainId, UUID linkId, LocalDate atTime) {
        this.userId = userId;
        this.clickId = clickId;
        this.domainId = domainId;
        this.linkId = linkId;
        this.atTime = atTime;
    }
}
