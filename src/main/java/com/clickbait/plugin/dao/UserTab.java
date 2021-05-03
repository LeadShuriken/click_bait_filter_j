package com.clickbait.plugin.dao;

import java.util.Map;
import java.util.UUID;

import javax.validation.constraints.Min;

import com.clickbait.plugin.annotations.UUIDA;
import com.clickbait.plugin.annotations.SQLInjectionSafe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTab {

    @Min(1)
    @JsonProperty("tabId")
    private final Integer tabId;

    @UUIDA
    @JsonProperty("domainId")
    private final UUID domainId;

    @UUIDA
    @JsonProperty("userId")
    private final UUID userId;

    @SQLInjectionSafe
    @JsonProperty("name")
    private final String name;

    @JsonProperty("links")
    private final Map<String, Float> links;

    public UserTab(UUID userId, UUID domainId, int tabId, String name, Map<String, Float> links) {
        this.domainId = domainId;
        this.userId = userId;
        this.links = links;
        this.tabId = tabId;
        this.name = name;
    }

    public UserTab(int tabId, String name, Map<String, Float> links) {
        this.domainId = null;
        this.userId = null;
        this.links = links;
        this.tabId = tabId;
        this.name = name;
    }

    public UserTab() {
        this.links = null;
        this.userId = null;
        this.domainId = null;
        this.tabId = null;
        this.name = null;
    }

    public UUID getUserId() {
        return userId;
    }

    public Integer getTabId() {
        return tabId;
    }

    public Map<String, Float> getLinks() {
        return links;
    }

    public UUID getDomainId() {
        return domainId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UserTab{tabId=" + tabId + ", name=" + name + "}";
    }
}
