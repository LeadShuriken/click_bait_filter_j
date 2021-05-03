package com.clickbait.plugin.dao;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.Past;

import com.clickbait.plugin.annotations.SQLInjectionSafe;
import com.clickbait.plugin.annotations.UUIDA;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserClick {
    private final static String reg = "\\)|\\(";

    @UUIDA
    @JsonProperty("userId")
    private final UUID userId;

    @SQLInjectionSafe
    @JsonProperty("domain")
    private final String domain;

    @SQLInjectionSafe
    @JsonProperty("link")
    private final String link;

    @Past
    @JsonProperty("atTime")
    private final LocalDate atTime;

    @JsonProperty("score")
    private final Float score;

    public UserClick(UUID userId, String domain, String link, LocalDate atTime, Float score) {
        this.userId = userId;
        this.score = score;
        this.domain = domain;
        this.link = link;
        this.atTime = atTime;
    }

    public UserClick(String link, Float score) {
        this.score = score;
        this.link = link;
        this.userId = null;
        this.domain = null;
        this.atTime = null;
    }

    public UserClick() {
        this.score = null;
        this.userId = null;
        this.domain = null;
        this.link = null;
        this.atTime = null;
    }

    public Float getScore() {
        return score;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getDomain() {
        return domain;
    }

    public String getLink() {
        return link;
    }

    public LocalDate getAtTime() {
        return atTime;
    }

    public static UserClick valueOf(Object a) {
        String[] b = a.toString().replaceAll(reg, "").split(",");
        if (b.length == 2 && !Strings.isNullOrEmpty(b[0]) && !Strings.isNullOrEmpty(b[1])) {
            return new UserClick(b[0], Float.valueOf(b[1]));
        }
        return null;
    }

    @Override
    public String toString() {
        return "UserClick{userId=" + userId + ", domain=" + domain + ", link=" + link + ", atTime=" + atTime + "}";
    }
}
