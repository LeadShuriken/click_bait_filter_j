package com.clickbait.plugin.dao;

import java.util.List;
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
    private final List<Link> links;

    public UserTab(int tabId, String name, List<Link> links) {
        this.domainId = null;
        this.userId = null;
        this.links = links;
        this.tabId = tabId;
        this.name = name;
    }

    public UserTab() {
        this.domainId = null;
        this.userId = null;
        this.links = null;
        this.tabId = null;
        this.name = null;
    }

    public UUID getUserId() {
        return userId;
    }

    public Integer getTabId() {
        return tabId;
    }

    public List<Link> getLinks() {
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
