package com.isera.assetmanagement.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expiration = expiration;
    }

    public String generateToken(
            String username,
            Long tokenVersion
    ) {

        Date issuedAt = new Date();

        Date expiryDate = new Date(
                issuedAt.getTime() + expiration
        );

        return Jwts.builder()
                .subject(username)
                .claim(
                        "tokenVersion",
                        tokenVersion
                )
                .issuedAt(issuedAt)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(
            String token
    ) {

        return extractAllClaims(token)
                .getSubject();
    }

    public Long extractTokenVersion(
            String token
    ) {

        Number version =
                extractAllClaims(token)
                        .get(
                                "tokenVersion",
                                Number.class
                        );

        if (version == null) {
            throw new IllegalArgumentException(
                    "Token version is missing"
            );
        }

        return version.longValue();
    }

    public boolean isTokenValid(
            String token,
            String username,
            Long currentTokenVersion
    ) {

        Claims claims =
                extractAllClaims(token);

        String extractedUsername =
                claims.getSubject();

        Number tokenVersion =
                claims.get(
                        "tokenVersion",
                        Number.class
                );

        if (tokenVersion == null) {
            return false;
        }

        return extractedUsername.equals(username)
                && tokenVersion.longValue()
                == currentTokenVersion
                && !isTokenExpired(claims);
    }

    private boolean isTokenExpired(
            Claims claims
    ) {

        return claims
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(
            String token
    ) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}