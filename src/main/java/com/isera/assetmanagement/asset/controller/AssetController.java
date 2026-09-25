package com.isera.assetmanagement.asset.controller;

import com.isera.assetmanagement.asset.dto.AssetRequest;
import com.isera.assetmanagement.asset.dto.AssetResponse;
import com.isera.assetmanagement.asset.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public ResponseEntity<AssetResponse> createAsset(
            @Valid @RequestBody AssetRequest request
    ) {

        AssetResponse response =
                assetService.createAsset(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> getAssetById(
            @PathVariable Long id
    ) {

        AssetResponse response =
                assetService.getAssetById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAllAssets() {

        List<AssetResponse> response =
                assetService.getAllAssets();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponse> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody AssetRequest request
    ) {

        AssetResponse response =
                assetService.updateAsset(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(
            @PathVariable Long id
    ) {

        assetService.deleteAsset(id);

        return ResponseEntity.noContent().build();
    }
}