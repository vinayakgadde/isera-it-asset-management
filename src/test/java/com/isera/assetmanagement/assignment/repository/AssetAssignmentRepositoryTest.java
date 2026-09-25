package com.isera.assetmanagement.assignment.repository;

import com.isera.assetmanagement.assignment.entity.AssetAssignment;

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
class AssetAssignmentRepositoryTest {

    @Autowired
    private AssetAssignmentRepository assetAssignmentRepository;

    // =========================================================
    // TEST 1
    // Find assignment by ID
    // =========================================================

    @Test
    void shouldFindAssignmentById() {

        Optional<AssetAssignment> result =
                assetAssignmentRepository.findById(1L);

        assertTrue(result.isPresent());

        AssetAssignment assignment =
                result.get();

        assertEquals(
                1L,
                assignment.getId()
        );

        assertNotNull(
                assignment.getAsset()
        );

        assertNotNull(
                assignment.getEmployee()
        );

        assertNotNull(
                assignment.getAssignedBy()
        );

        assertEquals(
                "AST-LAP-001",
                assignment.getAsset().getAssetTag()
        );

        assertEquals(
                "Rahul Sharma",
                assignment.getEmployee().getFirstName()
                        + " "
                        + assignment.getEmployee().getLastName()
        );
    }

    // =========================================================
    // TEST 2
    // Find assignments by asset ID
    // =========================================================

    @Test
    void shouldFindAssignmentsByAssetId() {

        List<AssetAssignment> assignments =
                assetAssignmentRepository
                        .findByAssetId(2L);

        assertNotNull(assignments);

        assertFalse(
                assignments.isEmpty()
        );

        assertTrue(
                assignments.stream()
                        .allMatch(
                                assignment ->
                                        assignment.getAsset().getId() == 2L
                        )
        );
    }

    // =========================================================
    // TEST 3
    // Find assignments by employee ID
    // =========================================================

    @Test
    void shouldFindAssignmentsByEmployeeId() {

        List<AssetAssignment> assignments =
                assetAssignmentRepository
                        .findByEmployeeId(1L);

        assertNotNull(assignments);

        assertFalse(
                assignments.isEmpty()
        );

        assertTrue(
                assignments.stream()
                        .allMatch(
                                assignment ->
                                        assignment.getEmployee().getId() == 1L
                        )
        );
    }

    // =========================================================
    // TEST 4
    // Find assignments by status
    // =========================================================

    @Test
    void shouldFindReturnedAssignmentsByStatus() {

        List<AssetAssignment> assignments =
                assetAssignmentRepository
                        .findByStatus("RETURNED");

        assertNotNull(assignments);

        assertFalse(
                assignments.isEmpty()
        );

        assertTrue(
                assignments.stream()
                        .allMatch(
                                assignment ->
                                        "RETURNED".equals(
                                                assignment.getStatus()
                                        )
                        )
        );
    }

    // =========================================================
    // TEST 5
    // Find employee assignments by status
    // =========================================================

    @Test
    void shouldFindReturnedAssignmentsByEmployeeAndStatus() {

        List<AssetAssignment> assignments =
                assetAssignmentRepository
                        .findByEmployeeIdAndStatus(
                                1L,
                                "RETURNED"
                        );

        assertNotNull(assignments);

        assertFalse(
                assignments.isEmpty()
        );

        assertTrue(
                assignments.stream()
                        .allMatch(
                                assignment ->
                                        assignment.getEmployee().getId() == 1L
                                                && "RETURNED".equals(
                                                assignment.getStatus()
                                        )
                        )
        );
    }

    // =========================================================
    // TEST 6
    // Active assignment query
    // =========================================================

    @Test
    void shouldReturnEmptyWhenAssetHasNoActiveAssignment() {

        Optional<AssetAssignment> result =
                assetAssignmentRepository
                        .findByAssetIdAndStatus(
                                2L,
                                "ACTIVE"
                        );

        assertTrue(
                result.isEmpty()
        );
    }

    // =========================================================
    // TEST 7
    // Unknown asset should return empty list
    // =========================================================

    @Test
    void shouldReturnEmptyWhenAssetDoesNotExist() {

        List<AssetAssignment> assignments =
                assetAssignmentRepository
                        .findByAssetId(999999L);

        assertNotNull(assignments);

        assertTrue(
                assignments.isEmpty()
        );
    }

    // =========================================================
    // TEST 8
    // Unknown employee should return empty list
    // =========================================================

    @Test
    void shouldReturnEmptyWhenEmployeeDoesNotExist() {

        List<AssetAssignment> assignments =
                assetAssignmentRepository
                        .findByEmployeeId(999999L);

        assertNotNull(assignments);

        assertTrue(
                assignments.isEmpty()
        );
    }
}