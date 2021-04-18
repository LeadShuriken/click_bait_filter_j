package com.clickbait.plugin.security;

public enum ApplicationUserPrivilege {
    USERS_READ("USERS_READ"),
    USERS_WRITE("USERS_WRITE"),
    CLICKS_READ("CLICKS_READ"),
    CLICKS_WRITE("CLICKS_WRITE"),
    DOMAINS_READ("DOMAINS_READ"),
    DOMAINS_WRITE("DOMAINS_WRITE");

    private final String privilege;

    private ApplicationUserPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getPrivilege () {
        return privilege;
    }
}