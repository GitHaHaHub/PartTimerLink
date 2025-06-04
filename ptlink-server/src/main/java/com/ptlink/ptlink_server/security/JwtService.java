package com.ptlink.ptlink_server.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;
    
    private SecretKey secretKey;

    // Ideally, load this key from a secure config or secret manager
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateAccessToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(3600); // 1 hour

        return Jwts.builder()
                .subject(subject)
                .signWith(secretKey)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claims(claims)
                .compact();
    }
}