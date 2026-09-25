package com.isera.assetmanagement.assignment.repository;

import com.isera.assetmanagement.assignment.entity.AssetAssignment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetAssignmentRepository
        extends JpaRepository<AssetAssignment, Long> {

    List<AssetAssignment> findByAssetId(Long assetId);

    List<AssetAssignment> findByEmployeeId(Long employeeId);

    List<AssetAssignment> findByStatus(String status);

    @Override
    @EntityGraph(attributePaths = {
            "asset",
            "asset.category",
            "asset.location",
            "employee",
            "employee.department"
    })
    List<AssetAssignment> findAll();

    @EntityGraph(attributePaths = {
            "asset",
            "asset.category",
            "asset.location",
            "employee"
    })
    List<AssetAssignment> findByEmployeeIdAndStatus(
            Long employeeId,
            String status
    );

    @EntityGraph(attributePaths = {
            "asset",
            "employee",
            "assignedBy",
            "returnedBy"
    })
    Optional<AssetAssignment> findById(Long id);

    Optional<AssetAssignment> findByAssetIdAndStatus(
            Long assetId,
            String status
    );
}