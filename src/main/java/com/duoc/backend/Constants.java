package com.duoc.backend;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Properties;

public final class Constants {

    // Spring Security
    public static final String LOGIN_URL = "/login";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    // JWT
    public static final String ISSUER_INFO = "https://www.duocuc.cl/";
    public static final long TOKEN_EXPIRATION_TIME = 864_000_000; // 1 day

    private Constants() {
        // Utility class
    }

    public static String getJwtSecret() {
        String envKey = System.getenv("JWT_SECRET");
        if (envKey != null && !envKey.isBlank()) {
            return envKey;
        }
        String envSecretKey = System.getenv("JWT_SECRET_KEY");
        if (envSecretKey != null && !envSecretKey.isBlank()) {
            return envSecretKey;
        }
        String propKey = System.getProperty("jwt.secret");
        if (propKey != null && !propKey.isBlank() && !propKey.startsWith("${")) {
            return propKey;
        }
        try (InputStream is = Constants.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                String configuredKey = props.getProperty("jwt.secret");
                if (configuredKey != null && !configuredKey.isBlank() && !configuredKey.startsWith("${")) {
                    return configuredKey;
                }
            }
        } catch (Exception ignored) {
            // Ignored
        }
        return "ZnJhc2VzbGFyZ2FzcGFyYWNvbG9jYXJjb21vY2xhdmVlbnVucHJvamVjdG9kZWVtZXBsb3BhcmFqd3Rjb25zcHJpbmdzZWN1cml0eQ==bWlwcnVlYmFkZWVqbXBsb3BhcmFiYXNlNjQ=";
    }

    public static Key getSigningKey() {
        return getSigningKey(getJwtSecret());
    }

    public static Key getSigningKeyB64(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static Key getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}