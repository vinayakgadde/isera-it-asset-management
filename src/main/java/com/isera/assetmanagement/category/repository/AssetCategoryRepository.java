package com.isera.assetmanagement.category.repository;

import com.isera.assetmanagement.category.entity.AssetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetCategoryRepository
        extends JpaRepository<AssetCategory, Long> {

    Optional<AssetCategory> findByCode(String code);

    Optional<AssetCategory> findByName(String name);

    boolean existsByCode(String code);

    boolean existsByName(String name);
}