package com.clickbait.plugin.security;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ApiPasswordEncoder implements PasswordEncoder {

    private String salt;
    private final String algo;

    private static ApiPasswordEncoder sigleInstance = null;

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public static ApiPasswordEncoder ApiPasswordEncoder(String algo) {
        if (sigleInstance == null) {
            sigleInstance = new ApiPasswordEncoder(algo);
        }
        return sigleInstance;
    }

    private ApiPasswordEncoder(String algo) {
        this.algo = algo;
    }

    public String encode(CharSequence rawPassword) {
        String result = null;

        try {
            Mac sha512Hmac = Mac.getInstance(algo);
            final byte[] byteKey = Utf8.encode(salt);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, algo);
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(Utf8.encode(rawPassword.toString()));
            result = Base64.getEncoder().encodeToString(macData);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return MessageDigest.isEqual(Utf8.encode(rawPassword), Utf8.encode(encodedPassword));
    }
}