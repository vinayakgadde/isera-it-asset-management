package com.isera.assetmanagement.auth.controller;

import com.isera.assetmanagement.auth.dto.LoginRequest;
import com.isera.assetmanagement.auth.dto.LoginResponse;
import com.isera.assetmanagement.auth.dto.RefreshTokenRequest;
import com.isera.assetmanagement.auth.dto.RefreshTokenResponse;
import com.isera.assetmanagement.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(
            AuthenticationService authenticationService
    ) {
        this.authenticationService =
                authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response =
                authenticationService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse>
    refreshAccessToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        RefreshTokenResponse response =
                authenticationService
                        .refreshAccessToken(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        authenticationService.logout(request);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}