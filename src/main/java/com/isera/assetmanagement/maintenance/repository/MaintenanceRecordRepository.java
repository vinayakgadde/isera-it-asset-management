package com.isera.assetmanagement.maintenance.repository;

import com.isera.assetmanagement.maintenance.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRecordRepository
        extends JpaRepository<MaintenanceRecord, Long> {

    List<MaintenanceRecord> findByAssetId(Long assetId);

    List<MaintenanceRecord> findByReportedById(Long employeeId);

    List<MaintenanceRecord> findByAssignedTechnicianId(Long userId);

    List<MaintenanceRecord> findByStatus(String status);

    List<MaintenanceRecord> findByAssetIdAndStatus(
            Long assetId,
            String status
    );
}