package com.isera.assetmanagement.auth.service;

import com.isera.assetmanagement.auth.dto.LoginRequest;
import com.isera.assetmanagement.auth.dto.LoginResponse;
import com.isera.assetmanagement.auth.dto.RefreshTokenRequest;
import com.isera.assetmanagement.auth.dto.RefreshTokenResponse;
import com.isera.assetmanagement.auth.entity.RefreshToken;
import com.isera.assetmanagement.security.jwt.JwtService;
import com.isera.assetmanagement.user.entity.User;
import com.isera.assetmanagement.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            RefreshTokenService refreshTokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public LoginResponse login(
            LoginRequest request
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Authenticated user could not be found"
                        )
                );

        String accessToken =
                jwtService.generateToken(
                        user.getUsername(),
                        user.getTokenVersion()
                );

        String refreshToken =
                refreshTokenService
                        .createRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken,
                "Bearer",
                900
        );
    }

    @Transactional
    public RefreshTokenResponse refreshAccessToken(
            RefreshTokenRequest request
    ) {

        RefreshToken oldRefreshToken =
                refreshTokenService
                        .validateRefreshToken(
                                request.getRefreshToken()
                        );

        User user =
                oldRefreshToken.getUser();

        String newAccessToken =
                jwtService.generateToken(
                        user.getUsername(),
                        user.getTokenVersion()
                );

        String newRefreshToken =
                refreshTokenService
                        .rotateRefreshToken(
                                oldRefreshToken
                        );

        return new RefreshTokenResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                900
        );
    }

    @Transactional
    public void logout(
            RefreshTokenRequest request
    ) {

        User user =
                refreshTokenService
                        .validateRefreshToken(
                                request.getRefreshToken()
                        )
                        .getUser();

        refreshTokenService.revokeToken(
                request.getRefreshToken()
        );

        /*
         * Increment token version.
         * This immediately invalidates every
         * access token issued with the old version.
         */
        user.setTokenVersion(
                user.getTokenVersion() + 1
        );

        userRepository.save(user);
    }
}