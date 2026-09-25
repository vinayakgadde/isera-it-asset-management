package com.isera.assetmanagement.user.controller;

import com.isera.assetmanagement.user.dto.MeResponse;
import com.isera.assetmanagement.user.dto.MyAssetResponse;
import com.isera.assetmanagement.user.dto.MyAssignmentResponse;
import com.isera.assetmanagement.user.service.MeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    private final MeService meService;

    public MeController(
            MeService meService
    ) {
        this.meService =
                meService;
    }

    @GetMapping
    public ResponseEntity<MeResponse> getCurrentUser() {

        return ResponseEntity.ok(
                meService.getCurrentUser()
        );
    }

    @GetMapping("/assets")
    public ResponseEntity<List<MyAssetResponse>>
    getMyAssets() {

        return ResponseEntity.ok(
                meService.getMyAssets()
        );
    }

    @GetMapping("/assignments")
    public ResponseEntity<List<MyAssignmentResponse>>
    getMyAssignments() {

        return ResponseEntity.ok(
                meService.getMyAssignments()
        );
    }
}