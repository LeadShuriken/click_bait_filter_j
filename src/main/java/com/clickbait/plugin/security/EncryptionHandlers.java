package com.clickbait.plugin.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@ConfigurationProperties(prefix = "encryption")
public class EncryptionHandlers {

    private String tokenPrefix;
    private String authHeader;
    private JWTConfig jwtConfig;
    private PBKDF2Config pbkdf2Config;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtConfig.getSecretkey()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretkey()).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String hash(String salt, String password) {
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(salt, pbkdf2Config.getIterations(),
                pbkdf2Config.getHashWidth());
        pbkdf2PasswordEncoder.setEncodeHashAsBase64(true);
        return pbkdf2PasswordEncoder.encode(password);
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
}
