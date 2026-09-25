package com.isera.assetmanagement.asset.repository;

import com.isera.assetmanagement.asset.entity.Asset;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    boolean existsByAssetTag(String assetTag);

    boolean existsBySerialNumber(String serialNumber);

    @EntityGraph(attributePaths = {
            "category",
            "vendor",
            "location"
    })
    List<Asset> findAll();

    @EntityGraph(attributePaths = {
            "category",
            "vendor",
            "location"
    })
    Optional<Asset> findById(Long id);
}