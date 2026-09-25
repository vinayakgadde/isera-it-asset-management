package com.isera.assetmanagement.auth.repository;

import com.isera.assetmanagement.auth.entity.RefreshToken;
import com.isera.assetmanagement.user.entity.User;
import com.isera.assetmanagement.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindByTokenHash() {

        User user = getAdminUser();

        String tokenHash =
                "test-hash-" + UUID.randomUUID();

        RefreshToken refreshToken =
                createRefreshToken(
                        user,
                        tokenHash,
                        false
                );

        RefreshToken saved =
                refreshTokenRepository.save(refreshToken);

        Optional<RefreshToken> result =
                refreshTokenRepository.findByTokenHash(tokenHash);

        assertTrue(result.isPresent());

        assertEquals(
                saved.getId(),
                result.get().getId()
        );

        assertEquals(
                tokenHash,
                result.get().getTokenHash()
        );

        assertEquals(
                user.getId(),
                result.get().getUser().getId()
        );
    }

    @Test
    void shouldFindTokensByUserId() {

        User user = getAdminUser();

        RefreshToken token1 =
                createRefreshToken(
                        user,
                        "hash-" + UUID.randomUUID(),
                        false
                );

        RefreshToken token2 =
                createRefreshToken(
                        user,
                        "hash-" + UUID.randomUUID(),
                        true
                );

        refreshTokenRepository.save(token1);
        refreshTokenRepository.save(token2);

        List<RefreshToken> result =
                refreshTokenRepository.findByUserId(
                        user.getId()
                );

        assertTrue(
                result.size() >= 2
        );

        assertTrue(
                result.stream()
                        .anyMatch(token ->
                                token.getTokenHash()
                                        .equals(token1.getTokenHash()))
        );

        assertTrue(
                result.stream()
                        .anyMatch(token ->
                                token.getTokenHash()
                                        .equals(token2.getTokenHash()))
        );
    }

    @Test
    void shouldFindOnlyNonRevokedTokens() {

        User user = getAdminUser();

        RefreshToken activeToken =
                createRefreshToken(
                        user,
                        "active-" + UUID.randomUUID(),
                        false
                );

        RefreshToken revokedToken =
                createRefreshToken(
                        user,
                        "revoked-" + UUID.randomUUID(),
                        true
                );

        refreshTokenRepository.save(activeToken);
        refreshTokenRepository.save(revokedToken);

        List<RefreshToken> result =
                refreshTokenRepository
                        .findByUserIdAndRevokedFalse(
                                user.getId()
                        );

        assertFalse(result.isEmpty());

        assertTrue(
                result.stream()
                        .anyMatch(token ->
                                token.getTokenHash()
                                        .equals(activeToken.getTokenHash()))
        );

        assertFalse(
                result.stream()
                        .anyMatch(token ->
                                token.getTokenHash()
                                        .equals(revokedToken.getTokenHash()))
        );
    }

    @Test
    void shouldDeleteTokensByUserId() {

        User user = getAdminUser();

        RefreshToken token1 =
                createRefreshToken(
                        user,
                        "delete-" + UUID.randomUUID(),
                        false
                );

        RefreshToken token2 =
                createRefreshToken(
                        user,
                        "delete-" + UUID.randomUUID(),
                        true
                );

        refreshTokenRepository.save(token1);
        refreshTokenRepository.save(token2);

        refreshTokenRepository.deleteByUserId(
                user.getId()
        );

        List<RefreshToken> result =
                refreshTokenRepository.findByUserId(
                        user.getId()
                );

        assertFalse(
                result.stream()
                        .anyMatch(token ->
                                token.getTokenHash()
                                        .equals(token1.getTokenHash()))
        );

        assertFalse(
                result.stream()
                        .anyMatch(token ->
                                token.getTokenHash()
                                        .equals(token2.getTokenHash()))
        );
    }

    @Test
    void shouldReturnEmptyWhenTokenHashDoesNotExist() {

        Optional<RefreshToken> result =
                refreshTokenRepository.findByTokenHash(
                        "unknown-token-hash-999999"
                );

        assertTrue(
                result.isEmpty()
        );
    }

    private User getAdminUser() {

        return userRepository
                .findByUsername("admin")
                .orElseThrow(
                        () -> new AssertionError(
                                "Admin user should exist"
                        )
                );
    }

    private RefreshToken createRefreshToken(
            User user,
            String tokenHash,
            boolean revoked
    ) {

        RefreshToken refreshToken =
                new RefreshToken();

        refreshToken.setTokenHash(
                tokenHash
        );

        refreshToken.setUser(
                user
        );

        refreshToken.setExpiryDate(
                LocalDateTime.now().plusDays(30)
        );

        refreshToken.setRevoked(
                revoked
        );

        return refreshToken;
    }
}