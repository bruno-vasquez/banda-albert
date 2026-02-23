package com.alumnos.banda.springboot.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMillis;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-minutes:480}") long expirationMinutes
    ) {
        // Debe ser >= 32 bytes (256 bits)
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(secretBytes);
        this.expirationMillis = expirationMinutes * 60_000;
    }

    // ✅ Método simple (por si lo usas en otro lado)
    public String generateToken(String username) {
        return generateToken(username, null, null);
    }

    // ✅ Método que tu AuthController está esperando (username, role, userId)
    public String generateToken(String username, String role, Integer userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMillis);

        var builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256);

        if (role != null && !role.isBlank()) {
            builder.claim("role", role);
        }
        if (userId != null) {
            builder.claim("userId", userId);
        }

        return builder.compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}