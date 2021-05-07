package com.clickbait.plugin.dao;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.Past;

import com.clickbait.plugin.annotations.SQLInjectionSafe;
import com.clickbait.plugin.annotations.UUIDA;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserClick {

    @UUIDA
    @JsonProperty("userId")
    private final UUID userId;

    @SQLInjectionSafe
    @JsonProperty("domain")
    private final String domain;

    @JsonProperty("link")
    private final Link link;

    @Past
    @JsonProperty("atTime")
    private final LocalDate atTime;

    public UserClick(UUID userId, String domain, Link link, LocalDate atTime) {
        this.userId = userId;
        this.domain = domain;
        this.atTime = atTime;
        this.link = link;
    }

    public UserClick(UUID userId, Link link, LocalDate atTime) {
        this.atTime = atTime;
        this.userId = userId;
        this.link = link;
        this.domain = null;
    }

    public UserClick(Link link) {
        this.link = link;
        this.userId = null;
        this.domain = null;
        this.atTime = null;
    }

    public UserClick() {
        this.userId = null;
        this.domain = null;
        this.link = null;
        this.atTime = null;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getDomain() {
        return domain;
    }

    public Link getLink() {
        return link;
    }

    public LocalDate getAtTime() {
        return atTime;
    }

    @Override
    public String toString() {
        return "UserClick{userId=" + userId + ", domain=" + domain + ", link=" + link + ", atTime=" + atTime + "}";
    }
}
