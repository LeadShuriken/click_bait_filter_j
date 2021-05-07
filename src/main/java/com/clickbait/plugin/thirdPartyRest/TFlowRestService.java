package com.clickbait.plugin.thirdPartyRest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.clickbait.plugin.dao.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Service
@ConfigurationProperties(prefix = "tflow")
public class TFlowRestService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${encryption.authHeader}")
    private String authHeader;

    private URI api;
    private Endpoint getScore;
    private Endpoint getScores;

    public URI getApi() {
        return api;
    }

    public void setApi(URI api) {
        this.api = api;
    }

    public Endpoint getGetScore() {
        return getScore;
    }

    public void setGetScore(Endpoint getScore) {
        this.getScore = getScore;
    }

    public Endpoint getGetScores() {
        return getScores;
    }

    public void setGetScores(Endpoint getScores) {
        this.getScores = getScores;
    }

    public Link getScore(HttpServletRequest request, Link data) {
        final String uri = String.format("%s/%s", api, getScore.getPath());

        ResponseEntity<Link> response = restTemplate.exchange(uri, getScore.getType(), addE(request, data), Link.class);

        return response.getBody();
    }

    public List<Link> getScores(HttpServletRequest request, UserTab data) {
        final String uri = String.format("%s/%s", api, getScores.getPath());

        ResponseEntity<UserTab> response = restTemplate.exchange(uri, getScores.getType(), addE(request, data),
                UserTab.class);

        return response.getBody().getLinks();
    }

    private <T> HttpEntity<T> addE(HttpServletRequest request, T data) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(authHeader, request.getHeader(authHeader));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<T> entity = new HttpEntity<T>(data, headers);
        return entity;
    }
}
