package com.isera.assetmanagement.category.service.impl;

import com.isera.assetmanagement.category.dto.AssetCategoryRequest;
import com.isera.assetmanagement.category.dto.AssetCategoryResponse;
import com.isera.assetmanagement.category.entity.AssetCategory;
import com.isera.assetmanagement.category.repository.AssetCategoryRepository;
import com.isera.assetmanagement.category.service.AssetCategoryService;
import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssetCategoryServiceImpl
        implements AssetCategoryService {

    private final AssetCategoryRepository assetCategoryRepository;

    public AssetCategoryServiceImpl(
            AssetCategoryRepository assetCategoryRepository
    ) {
        this.assetCategoryRepository = assetCategoryRepository;
    }

    @Override
    public AssetCategoryResponse createAssetCategory(
            AssetCategoryRequest request
    ) {

        if (assetCategoryRepository.existsByCode(request.getCode())) {

            throw new DuplicateResourceException(
                    "Asset category code already exists: "
                            + request.getCode()
            );
        }

        if (assetCategoryRepository.existsByName(request.getName())) {

            throw new DuplicateResourceException(
                    "Asset category name already exists: "
                            + request.getName()
            );
        }

        AssetCategory assetCategory =
                new AssetCategory();

        assetCategory.setName(request.getName());
        assetCategory.setCode(request.getCode());
        assetCategory.setDescription(request.getDescription());
        assetCategory.setActive(request.getActive());

        AssetCategory savedAssetCategory =
                assetCategoryRepository.save(assetCategory);

        return mapToResponse(savedAssetCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetCategoryResponse getAssetCategoryById(
            Long id
    ) {

        AssetCategory assetCategory =
                assetCategoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asset category not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(assetCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetCategoryResponse>
    getAllAssetCategories() {

        return assetCategoryRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AssetCategoryResponse updateAssetCategory(
            Long id,
            AssetCategoryRequest request
    ) {

        AssetCategory assetCategory =
                assetCategoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asset category not found with id: "
                                                + id
                                )
                        );

        if (!assetCategory.getCode().equals(request.getCode())
                && assetCategoryRepository
                .existsByCode(request.getCode())) {

            throw new DuplicateResourceException(
                    "Asset category code already exists: "
                            + request.getCode()
            );
        }

        if (!assetCategory.getName().equals(request.getName())
                && assetCategoryRepository
                .existsByName(request.getName())) {

            throw new DuplicateResourceException(
                    "Asset category name already exists: "
                            + request.getName()
            );
        }

        assetCategory.setName(request.getName());
        assetCategory.setCode(request.getCode());
        assetCategory.setDescription(request.getDescription());
        assetCategory.setActive(request.getActive());

        AssetCategory updatedAssetCategory =
                assetCategoryRepository.save(assetCategory);

        return mapToResponse(updatedAssetCategory);
    }

    @Override
    public void deleteAssetCategory(Long id) {

        AssetCategory assetCategory =
                assetCategoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asset category not found with id: "
                                                + id
                                )
                        );

        assetCategoryRepository.delete(assetCategory);
    }

    private AssetCategoryResponse mapToResponse(
            AssetCategory assetCategory
    ) {

        AssetCategoryResponse response =
                new AssetCategoryResponse();

        response.setId(assetCategory.getId());
        response.setName(assetCategory.getName());
        response.setCode(assetCategory.getCode());
        response.setDescription(assetCategory.getDescription());
        response.setActive(assetCategory.getActive());
        response.setCreatedAt(assetCategory.getCreatedAt());
        response.setUpdatedAt(assetCategory.getUpdatedAt());

        return response;
    }
}