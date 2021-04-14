package com.clickbait.plugin.dao;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPrivilege {

    @JsonProperty("userId")
    private final UUID userId;

    @JsonProperty("privilegeId")
    private final UUID privilegeId;

    public UserPrivilege(UUID userId, UUID privilegeId) {
        this.userId = userId;
        this.privilegeId = privilegeId;
    }
}
