package com.isera.assetmanagement.asset.service;

import com.isera.assetmanagement.asset.dto.AssetRequest;
import com.isera.assetmanagement.asset.dto.AssetResponse;

import java.util.List;

public interface AssetService {

    AssetResponse createAsset(AssetRequest request);

    AssetResponse getAssetById(Long id);

    List<AssetResponse> getAllAssets();

    AssetResponse updateAsset(Long id, AssetRequest request);

    void deleteAsset(Long id);
}