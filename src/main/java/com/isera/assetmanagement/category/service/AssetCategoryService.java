package com.isera.assetmanagement.category.service;

import com.isera.assetmanagement.category.dto.AssetCategoryRequest;
import com.isera.assetmanagement.category.dto.AssetCategoryResponse;

import java.util.List;

public interface AssetCategoryService {

    AssetCategoryResponse createAssetCategory(
            AssetCategoryRequest request
    );

    AssetCategoryResponse getAssetCategoryById(
            Long id
    );

    List<AssetCategoryResponse> getAllAssetCategories();

    AssetCategoryResponse updateAssetCategory(
            Long id,
            AssetCategoryRequest request
    );

    void deleteAssetCategory(Long id);
}