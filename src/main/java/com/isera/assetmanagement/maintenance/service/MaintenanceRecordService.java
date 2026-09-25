package com.isera.assetmanagement.maintenance.service;

import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordRequest;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordResponse;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordUpdateRequest;

import java.util.List;

public interface MaintenanceRecordService {

    MaintenanceRecordResponse createMaintenance(
            MaintenanceRecordRequest request
    );

    MaintenanceRecordResponse getMaintenanceById(
            Long id
    );

    List<MaintenanceRecordResponse> getAllMaintenance();

    MaintenanceRecordResponse updateMaintenance(
            Long id,
            MaintenanceRecordUpdateRequest request
    );

    MaintenanceRecordResponse startMaintenance(
            Long id
    );

    MaintenanceRecordResponse resolveMaintenance(
            Long id,
            MaintenanceRecordUpdateRequest request
    );

    MaintenanceRecordResponse closeMaintenance(
            Long id
    );
}