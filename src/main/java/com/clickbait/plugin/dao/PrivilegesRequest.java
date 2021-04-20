package com.clickbait.plugin.dao;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;

import com.clickbait.plugin.annotations.UUIDA;
import com.clickbait.plugin.security.ApplicationUserPrivilege;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivilegesRequest {

    @UUIDA
    @JsonProperty("userId")
    private final UUID userId;

    @NotEmpty
    @JsonProperty("privileges")
    private final List<ApplicationUserPrivilege> privileges;

    public PrivilegesRequest(UUID userId, List<ApplicationUserPrivilege> privileges) {
        this.userId = userId;
        this.privileges = privileges;
    }

    public PrivilegesRequest() {
        this.userId = null;
        this.privileges = null;
    }

    public List<ApplicationUserPrivilege> getPrivileges() {
        return privileges;
    }

    public UUID getUserId() {
        return userId;
    }
}
