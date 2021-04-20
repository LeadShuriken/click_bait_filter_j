package com.clickbait.plugin.security;

public class MacConfig {
    private String salt;
    private String algorithm;

    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getAlgorithm() {
        return algorithm;
    }
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
