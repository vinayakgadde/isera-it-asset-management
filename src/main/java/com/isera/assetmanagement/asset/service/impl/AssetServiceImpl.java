package com.isera.assetmanagement.asset.service.impl;

import com.isera.assetmanagement.asset.dto.AssetRequest;
import com.isera.assetmanagement.asset.dto.AssetResponse;
import com.isera.assetmanagement.asset.entity.Asset;
import com.isera.assetmanagement.asset.repository.AssetRepository;
import com.isera.assetmanagement.asset.service.AssetService;
import com.isera.assetmanagement.category.entity.AssetCategory;
import com.isera.assetmanagement.category.repository.AssetCategoryRepository;
import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.exception.ResourceNotFoundException;
import com.isera.assetmanagement.location.entity.Location;
import com.isera.assetmanagement.location.repository.LocationRepository;
import com.isera.assetmanagement.vendor.entity.Vendor;
import com.isera.assetmanagement.vendor.repository.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final VendorRepository vendorRepository;
    private final LocationRepository locationRepository;

    public AssetServiceImpl(
            AssetRepository assetRepository,
            AssetCategoryRepository assetCategoryRepository,
            VendorRepository vendorRepository,
            LocationRepository locationRepository
    ) {
        this.assetRepository = assetRepository;
        this.assetCategoryRepository = assetCategoryRepository;
        this.vendorRepository = vendorRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public AssetResponse createAsset(
            AssetRequest request
    ) {

        if (assetRepository.existsByAssetTag(
                request.getAssetTag())) {

            throw new DuplicateResourceException(
                    "Asset tag already exists: "
                            + request.getAssetTag()
            );
        }

        if (request.getSerialNumber() != null
                && assetRepository.existsBySerialNumber(
                request.getSerialNumber())) {

            throw new DuplicateResourceException(
                    "Serial number already exists: "
                            + request.getSerialNumber()
            );
        }

        AssetCategory category =
                assetCategoryRepository.findById(
                        request.getCategoryId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Asset category not found with id: "
                                        + request.getCategoryId()
                        )
                );

        Location location =
                locationRepository.findById(
                        request.getLocationId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found with id: "
                                        + request.getLocationId()
                        )
                );

        Vendor vendor = null;

        if (request.getVendorId() != null) {

            vendor =
                    vendorRepository.findById(
                            request.getVendorId()
                    ).orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Vendor not found with id: "
                                            + request.getVendorId()
                            )
                    );
        }

        Asset asset = new Asset();

        asset.setAssetTag(
                request.getAssetTag()
        );

        asset.setSerialNumber(
                request.getSerialNumber()
        );

        asset.setCategory(
                category
        );

        asset.setVendor(
                vendor
        );

        asset.setBrand(
                request.getBrand()
        );

        asset.setModel(
                request.getModel()
        );

        asset.setPurchaseDate(
                request.getPurchaseDate()
        );

        asset.setPurchaseCost(
                request.getPurchaseCost()
        );

        asset.setWarrantyExpiryDate(
                request.getWarrantyExpiryDate()
        );

        asset.setLocation(
                location
        );

        asset.setStatus(
                request.getStatus()
        );

        asset.setCondition(
                request.getCondition()
        );

        asset.setDescription(
                request.getDescription()
        );

        Asset savedAsset =
                assetRepository.save(asset);

        return mapToResponse(
                savedAsset
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AssetResponse getAssetById(
            Long id
    ) {

        Asset asset =
                assetRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asset not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(asset);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetResponse> getAllAssets() {

        return assetRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AssetResponse updateAsset(
            Long id,
            AssetRequest request
    ) {

        Asset asset =
                assetRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asset not found with id: "
                                                + id
                                )
                        );

        if (!asset.getAssetTag().equals(
                request.getAssetTag())
                && assetRepository.existsByAssetTag(
                request.getAssetTag())) {

            throw new DuplicateResourceException(
                    "Asset tag already exists: "
                            + request.getAssetTag()
            );
        }

        if (request.getSerialNumber() != null
                && !request.getSerialNumber().equals(
                asset.getSerialNumber())
                && assetRepository.existsBySerialNumber(
                request.getSerialNumber())) {

            throw new DuplicateResourceException(
                    "Serial number already exists: "
                            + request.getSerialNumber()
            );
        }

        AssetCategory category =
                assetCategoryRepository.findById(
                        request.getCategoryId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Asset category not found with id: "
                                        + request.getCategoryId()
                        )
                );

        Location location =
                locationRepository.findById(
                        request.getLocationId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found with id: "
                                        + request.getLocationId()
                        )
                );

        Vendor vendor = null;

        if (request.getVendorId() != null) {

            vendor =
                    vendorRepository.findById(
                            request.getVendorId()
                    ).orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Vendor not found with id: "
                                            + request.getVendorId()
                            )
                    );
        }

        asset.setAssetTag(
                request.getAssetTag()
        );

        asset.setSerialNumber(
                request.getSerialNumber()
        );

        asset.setCategory(
                category
        );

        asset.setVendor(
                vendor
        );

        asset.setBrand(
                request.getBrand()
        );

        asset.setModel(
                request.getModel()
        );

        asset.setPurchaseDate(
                request.getPurchaseDate()
        );

        asset.setPurchaseCost(
                request.getPurchaseCost()
        );

        asset.setWarrantyExpiryDate(
                request.getWarrantyExpiryDate()
        );

        asset.setLocation(
                location
        );

        asset.setStatus(
                request.getStatus()
        );

        asset.setCondition(
                request.getCondition()
        );

        asset.setDescription(
                request.getDescription()
        );

        Asset updatedAsset =
                assetRepository.save(asset);

        return mapToResponse(
                updatedAsset
        );
    }

    @Override
    public void deleteAsset(
            Long id
    ) {

        Asset asset =
                assetRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asset not found with id: "
                                                + id
                                )
                        );

        assetRepository.delete(asset);
    }

    private AssetResponse mapToResponse(
            Asset asset
    ) {

        AssetResponse response =
                new AssetResponse();

        response.setId(
                asset.getId()
        );

        response.setAssetTag(
                asset.getAssetTag()
        );

        response.setSerialNumber(
                asset.getSerialNumber()
        );

        if (asset.getCategory() != null) {

            response.setCategoryId(
                    asset.getCategory().getId()
            );

            response.setCategoryName(
                    asset.getCategory().getName()
            );
        }

        if (asset.getVendor() != null) {

            response.setVendorId(
                    asset.getVendor().getId()
            );

            response.setVendorName(
                    asset.getVendor().getName()
            );
        }

        response.setBrand(
                asset.getBrand()
        );

        response.setModel(
                asset.getModel()
        );

        response.setPurchaseDate(
                asset.getPurchaseDate()
        );

        response.setPurchaseCost(
                asset.getPurchaseCost()
        );

        response.setWarrantyExpiryDate(
                asset.getWarrantyExpiryDate()
        );

        if (asset.getLocation() != null) {

            response.setLocationId(
                    asset.getLocation().getId()
            );

            response.setLocationName(
                    asset.getLocation().getName()
            );
        }

        response.setStatus(
                asset.getStatus()
        );

        response.setCondition(
                asset.getCondition()
        );

        response.setDescription(
                asset.getDescription()
        );

        response.setCreatedAt(
                asset.getCreatedAt()
        );

        response.setUpdatedAt(
                asset.getUpdatedAt()
        );

        return response;
    }
}