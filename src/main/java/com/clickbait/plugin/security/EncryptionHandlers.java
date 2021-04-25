package com.clickbait.plugin.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import com.clickbait.plugin.dao.User;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
@ConfigurationProperties(prefix = "encryption")
public class EncryptionHandlers {

    private String tokenPrefix;
    private String authHeader;
    private String adminNameHeader;
    private String adminPasswordHeader;
    private JWTConfig jwtConfig;
    private PBKDF2Config pbkdf2Config;
    private MacConfig passwordEncoder;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public PasswordEncoder getMacPasswordEncoder() {
        return ApiPasswordEncoder.ApiPasswordEncoder(passwordEncoder.getAlgorithm());
    }

    public PasswordEncoder getMacPasswordEncoder(String salt) {
        ApiPasswordEncoder apiPasswordEncoder = ApiPasswordEncoder.ApiPasswordEncoder(passwordEncoder.getAlgorithm());
        apiPasswordEncoder.setSalt(salt);
        return apiPasswordEncoder;
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String createToken(Authentication authResult) {
        return Jwts.builder().setSubject(authResult.getName()).claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(getSecretKey()).compact();
    }

    public Boolean validateToken(String token, String userNameP) {
        final String username = extractUsername(token);
        return (username.equals(userNameP) && !isTokenExpired(token));
    }

    public String pbkdf2Hash(String password) {
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(passwordEncoder.getSalt(),
                pbkdf2Config.getIterations(), pbkdf2Config.getHashWidth());
        pbkdf2PasswordEncoder.setEncodeHashAsBase64(true);
        return pbkdf2PasswordEncoder.encode(password);
    }

    public Boolean pbkdf2Matches(String row, String encoded) {
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(passwordEncoder.getSalt(),
                pbkdf2Config.getIterations(), pbkdf2Config.getHashWidth());
        pbkdf2PasswordEncoder.setEncodeHashAsBase64(true);
        return pbkdf2PasswordEncoder.matches(row, encoded);
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getAuthHeader() {
        return authHeader;
    }

    public String getAuthHeader(HttpServletRequest request) {
        return request.getHeader(authHeader).substring(tokenPrefix.length() + 1);
    }

    public String getAuthHeader(String header) {
        return header.substring(tokenPrefix.length() + 1);
    }

    public User getAdminFromHeader(HttpServletRequest request) {
        final String username = request.getHeader(adminNameHeader);
        final String password = request.getHeader(adminPasswordHeader);
        return new User(username, password);
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    public JWTConfig getJwtConfig() {
        return jwtConfig;
    }

    public void setJwtConfig(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public PBKDF2Config getPbkdf2Config() {
        return pbkdf2Config;
    }

    public void setPbkdf2Config(PBKDF2Config pbkdf2Config) {
        this.pbkdf2Config = pbkdf2Config;
    }

    public MacConfig getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(MacConfig passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String getAdminNameHeader() {
        return adminNameHeader;
    }

    public void setAdminNameHeader(String adminNameHeader) {
        this.adminNameHeader = adminNameHeader;
    }

    public String getAdminPasswordHeader() {
        return adminPasswordHeader;
    }

    public void setAdminPasswordHeader(String adminPasswordHeader) {
        this.adminPasswordHeader = adminPasswordHeader;
    }
}
