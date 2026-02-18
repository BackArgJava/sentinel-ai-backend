package com.sentinel.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 1. We create a highly secure, cryptographic secret key
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 2. The wristband is valid for 1 hour (3,600,000 milliseconds)
    private static final long EXPIRATION_TIME = 3600000;

    // 3. This method PRINTS the VIP wristband (The Token)
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // We put the username on the wristband
                .setIssuedAt(new Date()) // We print the current time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // When it expires
                .signWith(SECRET_KEY) // We stamp it with our unforgeable secret key!
                .compact();
    }

    // 4. This method READS the name on the wristband
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 5. This method CHECKS if the wristband is fake or expired
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true; // It's a real wristband!
        } catch (Exception e) {
            return false; // It's fake or expired!
        }
    }
}