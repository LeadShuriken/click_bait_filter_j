package com.clickbait.plugin.thirdPartyRest;

import org.springframework.http.HttpMethod;

import java.net.URI;

public class Endpoint {
    private URI path;
    private HttpMethod type;
    private boolean authenticated;

    public URI getPath() {
        return path;
    }

    public void setPath(URI path) {
        this.path = path;
    }

    public HttpMethod getType() {
        return type;
    }

    public void setType(HttpMethod type) {
        this.type = type;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}