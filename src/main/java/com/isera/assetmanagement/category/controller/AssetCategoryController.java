package com.isera.assetmanagement.category.controller;

import com.isera.assetmanagement.category.dto.AssetCategoryRequest;
import com.isera.assetmanagement.category.dto.AssetCategoryResponse;
import com.isera.assetmanagement.category.service.AssetCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asset-categories")
public class AssetCategoryController {

    private final AssetCategoryService assetCategoryService;

    public AssetCategoryController(
            AssetCategoryService assetCategoryService
    ) {
        this.assetCategoryService =
                assetCategoryService;
    }

    @PostMapping
    public ResponseEntity<AssetCategoryResponse>
    createAssetCategory(
            @Valid @RequestBody AssetCategoryRequest request
    ) {

        AssetCategoryResponse response =
                assetCategoryService
                        .createAssetCategory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetCategoryResponse>
    getAssetCategoryById(
            @PathVariable Long id
    ) {

        AssetCategoryResponse response =
                assetCategoryService
                        .getAssetCategoryById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AssetCategoryResponse>>
    getAllAssetCategories() {

        List<AssetCategoryResponse> response =
                assetCategoryService
                        .getAllAssetCategories();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetCategoryResponse>
    updateAssetCategory(
            @PathVariable Long id,
            @Valid @RequestBody AssetCategoryRequest request
    ) {

        AssetCategoryResponse response =
                assetCategoryService
                        .updateAssetCategory(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssetCategory(
            @PathVariable Long id
    ) {

        assetCategoryService.deleteAssetCategory(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}