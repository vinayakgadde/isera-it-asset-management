package com.isera.assetmanagement.asset.repository;

import com.isera.assetmanagement.asset.entity.Asset;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class AssetRepositoryTest {

    @Autowired
    private AssetRepository assetRepository;

    // =========================================================
    // TEST 1
    // Find existing asset by ID
    // =========================================================

    @Test
    void shouldFindAssetById() {

        Optional<Asset> result =
                assetRepository.findById(2L);

        assertTrue(result.isPresent());

        Asset asset = result.get();

        assertEquals(
                2L,
                asset.getId()
        );

        assertEquals(
                "AST-LAP-001",
                asset.getAssetTag()
        );

        assertEquals(
                "DELL-SN-001",
                asset.getSerialNumber()
        );

        assertEquals(
                "Dell",
                asset.getBrand()
        );

        assertEquals(
                "Latitude 5440",
                asset.getModel()
        );

        assertEquals(
                "IN_STOCK",
                asset.getStatus()
        );
    }

    // =========================================================
    // TEST 2
    // Find all assets
    // =========================================================

    @Test
    void shouldFindAllAssets() {

        List<Asset> assets =
                assetRepository.findAll();

        assertNotNull(assets);

        assertFalse(
                assets.isEmpty()
        );
    }

    // =========================================================
    // TEST 3
    // Verify relationships are loaded
    // =========================================================

    @Test
    void shouldLoadAssetRelationships() {

        Optional<Asset> result =
                assetRepository.findById(2L);

        assertTrue(result.isPresent());

        Asset asset = result.get();

        // Category
        assertNotNull(
                asset.getCategory()
        );

        assertEquals(
                "Laptop",
                asset.getCategory().getName()
        );

        // Location
        assertNotNull(
                asset.getLocation()
        );

        assertEquals(
                "Pune Office",
                asset.getLocation().getName()
        );

        // Vendor is optional in the current asset
        assertNull(
                asset.getVendor()
        );
    }

    // =========================================================
    // TEST 4
    // Check asset tag exists
    // =========================================================

    @Test
    void shouldReturnTrueWhenAssetTagExists() {

        boolean exists =
                assetRepository.existsByAssetTag(
                        "AST-LAP-001"
                );

        assertTrue(exists);
    }

    // =========================================================
    // TEST 5
    // Check asset tag does not exist
    // =========================================================

    @Test
    void shouldReturnFalseWhenAssetTagDoesNotExist() {

        boolean exists =
                assetRepository.existsByAssetTag(
                        "AST-NON-EXISTENT-999"
                );

        assertFalse(exists);
    }

    // =========================================================
    // TEST 6
    // Check serial number exists
    // =========================================================

    @Test
    void shouldReturnTrueWhenSerialNumberExists() {

        boolean exists =
                assetRepository.existsBySerialNumber(
                        "DELL-SN-001"
                );

        assertTrue(exists);
    }

    // =========================================================
    // TEST 7
    // Check serial number does not exist
    // =========================================================

    @Test
    void shouldReturnFalseWhenSerialNumberDoesNotExist() {

        boolean exists =
                assetRepository.existsBySerialNumber(
                        "SERIAL-NOT-FOUND-999"
                );

        assertFalse(exists);
    }

    // =========================================================
    // TEST 8
    // Missing asset ID
    // =========================================================

    @Test
    void shouldReturnEmptyWhenAssetDoesNotExist() {

        Optional<Asset> result =
                assetRepository.findById(999999L);

        assertTrue(
                result.isEmpty()
        );
    }
}