package com.isera.assetmanagement.auth.service;

import com.isera.assetmanagement.auth.entity.RefreshToken;
import com.isera.assetmanagement.auth.repository.RefreshTokenRepository;
import com.isera.assetmanagement.exception.InvalidTokenException;
import com.isera.assetmanagement.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final long refreshTokenExpiration;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${security.jwt.refresh-expiration}")
            long refreshTokenExpiration
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenExpiration =
                refreshTokenExpiration;
    }

    public String createRefreshToken(User user) {

        String rawToken =
                UUID.randomUUID()
                        + UUID.randomUUID().toString();

        RefreshToken refreshToken =
                new RefreshToken();

        refreshToken.setTokenHash(
                hashToken(rawToken)
        );

        refreshToken.setUser(user);

        refreshToken.setExpiryDate(
                LocalDateTime.now()
                        .plusSeconds(
                                refreshTokenExpiration
                        )
        );

        refreshToken.setRevoked(false);

        refreshTokenRepository.save(
                refreshToken
        );

        return rawToken;
    }

    public RefreshToken validateRefreshToken(
            String rawToken
    ) {

        String tokenHash =
                hashToken(rawToken);

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new InvalidTokenException(
                                        "Invalid refresh token"
                                )
                        );

        if (refreshToken.getRevoked()) {

            throw new InvalidTokenException(
                    "Refresh token has been revoked"
            );
        }

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new InvalidTokenException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }

    public String rotateRefreshToken(
            RefreshToken oldRefreshToken
    ) {

        oldRefreshToken.setRevoked(true);

        refreshTokenRepository.save(
                oldRefreshToken
        );

        return createRefreshToken(
                oldRefreshToken.getUser()
        );
    }

    public void revokeToken(
            String rawToken
    ) {

        String tokenHash =
                hashToken(rawToken);

        refreshTokenRepository
                .findByTokenHash(tokenHash)
                .ifPresent(refreshToken -> {

                    refreshToken.setRevoked(true);

                    refreshTokenRepository.save(
                            refreshToken
                    );
                });
    }

    private String hashToken(
            String rawToken
    ) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] hash =
                    digest.digest(
                            rawToken.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            return Base64.getEncoder()
                    .encodeToString(hash);

        } catch (NoSuchAlgorithmException exception) {

            throw new IllegalStateException(
                    "SHA-256 algorithm is not available",
                    exception
            );
        }
    }
}