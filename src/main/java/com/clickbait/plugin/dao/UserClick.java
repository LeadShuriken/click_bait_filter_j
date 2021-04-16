package com.clickbait.plugin.dao;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserClick {

    @JsonProperty("userId")
    private final UUID userId;

    @JsonProperty("domain")
    private final String domain;

    @JsonProperty("link")
    private final String link;

    @Past
    @JsonProperty("atTime")
    private final LocalDate atTime;

    public UserClick(UUID userId, String domain, String link, LocalDate atTime) {
        this.userId = userId;
        this.domain = domain;
        this.link = link;
        this.atTime = atTime;
    }

    @Override
    public String toString() {
        return "UserClick{userId=" + userId + ", domain=" + domain + ", link=" + link + ", atTime=" + atTime + "}";
    }
}
