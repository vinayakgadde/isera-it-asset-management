package com.isera.assetmanagement.auth.repository;

import com.isera.assetmanagement.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findByUserId(Long userId);

    List<RefreshToken> findByUserIdAndRevokedFalse(Long userId);

    void deleteByUserId(Long userId);
}