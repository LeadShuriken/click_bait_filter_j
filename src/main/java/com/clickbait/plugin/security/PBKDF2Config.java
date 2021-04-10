package com.clickbait.plugin.security;

public class PBKDF2Config {
    private int iterations;
    private int hashWidth;

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getHashWidth() {
        return hashWidth;
    }

    public void setHashWidth(int hashWidth) {
        this.hashWidth = hashWidth;
    }
}
