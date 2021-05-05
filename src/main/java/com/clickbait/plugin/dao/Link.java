package com.clickbait.plugin.dao;

import com.clickbait.plugin.annotations.SQLInjectionSafe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Link {
    private final static String reg = "\\)|\\(";

    @SQLInjectionSafe
    @JsonProperty("name")
    private final String name;

    @JsonProperty("score")
    private final Float score;

    public String getName() {
        return name;
    }

    public Float getScore() {
        return score;
    }

    public Link(String name, Float score) {
        this.name = name;
        this.score = score;
    }

    public Link() {
        this.score = null;
        this.name = null;
    }

    public static Link valueOf(Object a) {
        String[] b = a.toString().replaceAll(reg, "").split(",");
        return new Link(b[0], Float.valueOf(b[1]));
    }
}
