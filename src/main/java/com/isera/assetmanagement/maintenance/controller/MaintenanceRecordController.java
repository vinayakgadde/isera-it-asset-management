package com.isera.assetmanagement.maintenance.controller;

import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordRequest;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordResponse;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordUpdateRequest;
import com.isera.assetmanagement.maintenance.service.MaintenanceRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/maintenance")
public class MaintenanceRecordController {

    private final MaintenanceRecordService maintenanceRecordService;

    public MaintenanceRecordController(
            MaintenanceRecordService maintenanceRecordService
    ) {
        this.maintenanceRecordService = maintenanceRecordService;
    }

    @PostMapping
    public ResponseEntity<MaintenanceRecordResponse> createMaintenance(
            @Valid @RequestBody MaintenanceRecordRequest request
    ) {

        MaintenanceRecordResponse response =
                maintenanceRecordService.createMaintenance(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceRecordResponse>> getAllMaintenance() {

        return ResponseEntity.ok(
                maintenanceRecordService.getAllMaintenance()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponse> getMaintenanceById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                maintenanceRecordService.getMaintenanceById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponse> updateMaintenance(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceRecordUpdateRequest request
    ) {

        return ResponseEntity.ok(
                maintenanceRecordService.updateMaintenance(
                        id,
                        request
                )
        );
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<MaintenanceRecordResponse> startMaintenance(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                maintenanceRecordService.startMaintenance(id)
        );
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<MaintenanceRecordResponse> resolveMaintenance(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceRecordUpdateRequest request
    ) {

        return ResponseEntity.ok(
                maintenanceRecordService.resolveMaintenance(
                        id,
                        request
                )
        );
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<MaintenanceRecordResponse> closeMaintenance(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                maintenanceRecordService.closeMaintenance(id)
        );
    }
}