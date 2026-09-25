package com.isera.assetmanagement.maintenance.service.impl;

import com.isera.assetmanagement.asset.entity.Asset;
import com.isera.assetmanagement.asset.repository.AssetRepository;
import com.isera.assetmanagement.assignment.entity.AssetAssignment;
import com.isera.assetmanagement.assignment.repository.AssetAssignmentRepository;
import com.isera.assetmanagement.audit.service.AuditLogService;
import com.isera.assetmanagement.employee.entity.Employee;
import com.isera.assetmanagement.employee.repository.EmployeeRepository;
import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.exception.ResourceNotFoundException;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordRequest;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordResponse;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordUpdateRequest;
import com.isera.assetmanagement.maintenance.entity.MaintenanceRecord;
import com.isera.assetmanagement.maintenance.repository.MaintenanceRecordRepository;
import com.isera.assetmanagement.maintenance.service.MaintenanceRecordService;
import com.isera.assetmanagement.user.entity.User;
import com.isera.assetmanagement.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaintenanceRecordServiceImpl
        implements MaintenanceRecordService {

    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final AssetAssignmentRepository assetAssignmentRepository;
    private final AuditLogService auditLogService;

    public MaintenanceRecordServiceImpl(
            MaintenanceRecordRepository maintenanceRecordRepository,
            AssetRepository assetRepository,
            EmployeeRepository employeeRepository,
            UserRepository userRepository,
            AssetAssignmentRepository assetAssignmentRepository,
            AuditLogService auditLogService
    ) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
        this.assetRepository = assetRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.assetAssignmentRepository = assetAssignmentRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public MaintenanceRecordResponse createMaintenance(
            MaintenanceRecordRequest request
    ) {

        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Asset not found with id: "
                                        + request.getAssetId()
                        )
                );

        List<MaintenanceRecord> activeMaintenance =
                maintenanceRecordRepository.findByAssetIdAndStatus(
                        asset.getId(),
                        "OPEN"
                );

        if (!activeMaintenance.isEmpty()) {
            throw new DuplicateResourceException(
                    "Asset already has an active maintenance record"
            );
        }

        List<MaintenanceRecord> inProgressMaintenance =
                maintenanceRecordRepository.findByAssetIdAndStatus(
                        asset.getId(),
                        "IN_PROGRESS"
                );

        if (!inProgressMaintenance.isEmpty()) {
            throw new DuplicateResourceException(
                    "Asset already has an active maintenance record"
            );
        }

        if ("RETIRED".equals(asset.getStatus())
                || "DISPOSED".equals(asset.getStatus())) {

            throw new DuplicateResourceException(
                    "Maintenance cannot be created for asset with status: "
                            + asset.getStatus()
            );
        }

        Employee reportedBy = null;

        if (request.getReportedByEmployeeId() != null) {

            reportedBy = employeeRepository
                    .findById(request.getReportedByEmployeeId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Employee not found with id: "
                                            + request.getReportedByEmployeeId()
                            )
                    );

            if (!"ACTIVE".equals(reportedBy.getStatus())) {
                throw new DuplicateResourceException(
                        "Maintenance cannot be reported by inactive employee"
                );
            }
        }

        User assignedTechnician = null;

        if (request.getAssignedTechnicianId() != null) {

            assignedTechnician = userRepository
                    .findById(request.getAssignedTechnicianId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Technician user not found with id: "
                                            + request.getAssignedTechnicianId()
                            )
                    );

            if (!Boolean.TRUE.equals(assignedTechnician.getActive())) {
                throw new DuplicateResourceException(
                        "Assigned technician is inactive"
                );
            }
        }

        String oldAssetStatus = asset.getStatus();

        MaintenanceRecord maintenanceRecord =
                new MaintenanceRecord();

        maintenanceRecord.setAsset(asset);
        maintenanceRecord.setReportedBy(reportedBy);
        maintenanceRecord.setAssignedTechnician(assignedTechnician);
        maintenanceRecord.setIssueDescription(
                request.getIssueDescription()
        );
        maintenanceRecord.setReportedDate(LocalDateTime.now());
        maintenanceRecord.setStatus("OPEN");

        MaintenanceRecord savedRecord =
                maintenanceRecordRepository.save(maintenanceRecord);

        asset.setStatus("UNDER_MAINTENANCE");
        assetRepository.save(asset);

        String oldValue = String.format(
                "{\"assetStatus\":\"%s\"}",
                oldAssetStatus
        );

        String newValue = String.format(
                "{\"assetStatus\":\"UNDER_MAINTENANCE\",\"maintenanceId\":%d}",
                savedRecord.getId()
        );

        auditLogService.log(
                "MAINTENANCE_CREATED",
                "MAINTENANCE_RECORD",
                savedRecord.getId(),
                oldValue,
                newValue
        );

        return mapToResponse(savedRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceRecordResponse getMaintenanceById(
            Long id
    ) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(maintenanceRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecordResponse> getAllMaintenance() {

        return maintenanceRecordRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public MaintenanceRecordResponse updateMaintenance(
            Long id,
            MaintenanceRecordUpdateRequest request
    ) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: "
                                                + id
                                )
                        );

        if ("CLOSED".equals(maintenanceRecord.getStatus())) {
            throw new DuplicateResourceException(
                    "Closed maintenance record cannot be updated"
            );
        }

        String oldValue = buildAuditValue(maintenanceRecord);

        if (request.getAssignedTechnicianId() != null) {

            User technician = userRepository
                    .findById(request.getAssignedTechnicianId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Technician user not found with id: "
                                            + request.getAssignedTechnicianId()
                            )
                    );

            if (!Boolean.TRUE.equals(technician.getActive())) {
                throw new DuplicateResourceException(
                        "Assigned technician is inactive"
                );
            }

            maintenanceRecord.setAssignedTechnician(technician);
        }

        if (request.getResolution() != null) {
            maintenanceRecord.setResolution(
                    request.getResolution()
            );
        }

        if (request.getCost() != null) {
            maintenanceRecord.setCost(
                    request.getCost()
            );
        }

        MaintenanceRecord savedRecord =
                maintenanceRecordRepository.save(maintenanceRecord);

        String newValue = buildAuditValue(savedRecord);

        auditLogService.log(
                "MAINTENANCE_UPDATED",
                "MAINTENANCE_RECORD",
                savedRecord.getId(),
                oldValue,
                newValue
        );

        return mapToResponse(savedRecord);
    }

    @Override
    @Transactional
    public MaintenanceRecordResponse startMaintenance(
            Long id
    ) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: "
                                                + id
                                )
                        );

        if (!"OPEN".equals(maintenanceRecord.getStatus())) {
            throw new DuplicateResourceException(
                    "Maintenance can only be started when status is OPEN"
            );
        }

        String oldValue = String.format(
                "{\"status\":\"%s\"}",
                maintenanceRecord.getStatus()
        );

        maintenanceRecord.setStatus("IN_PROGRESS");
        maintenanceRecord.setStartedDate(LocalDateTime.now());

        MaintenanceRecord savedRecord =
                maintenanceRecordRepository.save(maintenanceRecord);

        String newValue = String.format(
                "{\"status\":\"IN_PROGRESS\",\"startedDate\":\"%s\"}",
                savedRecord.getStartedDate()
        );

        auditLogService.log(
                "MAINTENANCE_STARTED",
                "MAINTENANCE_RECORD",
                savedRecord.getId(),
                oldValue,
                newValue
        );

        return mapToResponse(savedRecord);
    }

    @Override
    @Transactional
    public MaintenanceRecordResponse resolveMaintenance(
            Long id,
            MaintenanceRecordUpdateRequest request
    ) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: "
                                                + id
                                )
                        );

        if (!"IN_PROGRESS".equals(maintenanceRecord.getStatus())) {
            throw new DuplicateResourceException(
                    "Maintenance can only be resolved when status is IN_PROGRESS"
            );
        }

        if (request.getResolution() == null
                || request.getResolution().isBlank()) {

            throw new IllegalArgumentException(
                    "Resolution is required to resolve maintenance"
            );
        }

        String oldValue = String.format(
                "{\"status\":\"%s\"}",
                maintenanceRecord.getStatus()
        );

        maintenanceRecord.setStatus("RESOLVED");
        maintenanceRecord.setResolution(
                request.getResolution()
        );
        maintenanceRecord.setResolvedDate(
                LocalDateTime.now()
        );

        if (request.getCost() != null) {
            maintenanceRecord.setCost(
                    request.getCost()
            );
        }

        MaintenanceRecord savedRecord =
                maintenanceRecordRepository.save(maintenanceRecord);

        String newValue = String.format(
                "{\"status\":\"RESOLVED\",\"resolution\":\"%s\"}",
                escapeJson(request.getResolution())
        );

        auditLogService.log(
                "MAINTENANCE_RESOLVED",
                "MAINTENANCE_RECORD",
                savedRecord.getId(),
                oldValue,
                newValue
        );

        return mapToResponse(savedRecord);
    }

    @Override
    @Transactional
    public MaintenanceRecordResponse closeMaintenance(
            Long id
    ) {

        MaintenanceRecord maintenanceRecord =
                maintenanceRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance record not found with id: "
                                                + id
                                )
                        );

        if (!"RESOLVED".equals(maintenanceRecord.getStatus())) {
            throw new DuplicateResourceException(
                    "Maintenance can only be closed when status is RESOLVED"
            );
        }

        Asset asset = maintenanceRecord.getAsset();

        String oldValue = String.format(
                "{\"maintenanceStatus\":\"%s\",\"assetStatus\":\"%s\"}",
                maintenanceRecord.getStatus(),
                asset.getStatus()
        );

        maintenanceRecord.setStatus("CLOSED");

        AssetAssignment activeAssignment =
                assetAssignmentRepository
                        .findByAssetIdAndStatus(
                                asset.getId(),
                                "ACTIVE"
                        )
                        .orElse(null);

        if (activeAssignment != null) {
            asset.setStatus("ASSIGNED");
        } else {
            asset.setStatus("IN_STOCK");
        }

        assetRepository.save(asset);

        MaintenanceRecord savedRecord =
                maintenanceRecordRepository.save(maintenanceRecord);

        String newValue = String.format(
                "{\"maintenanceStatus\":\"CLOSED\",\"assetStatus\":\"%s\"}",
                asset.getStatus()
        );

        auditLogService.log(
                "MAINTENANCE_CLOSED",
                "MAINTENANCE_RECORD",
                savedRecord.getId(),
                oldValue,
                newValue
        );

        return mapToResponse(savedRecord);
    }

    private MaintenanceRecordResponse mapToResponse(
            MaintenanceRecord maintenanceRecord
    ) {

        MaintenanceRecordResponse response =
                new MaintenanceRecordResponse();

        response.setId(maintenanceRecord.getId());

        Asset asset = maintenanceRecord.getAsset();

        if (asset != null) {
            response.setAssetId(asset.getId());
            response.setAssetTag(asset.getAssetTag());
        }

        Employee reportedBy =
                maintenanceRecord.getReportedBy();

        if (reportedBy != null) {
            response.setReportedByEmployeeId(
                    reportedBy.getId()
            );

            response.setReportedByEmployeeName(
                    reportedBy.getFirstName()
                            + " "
                            + reportedBy.getLastName()
            );
        }

        User assignedTechnician =
                maintenanceRecord.getAssignedTechnician();

        if (assignedTechnician != null) {
            response.setAssignedTechnicianId(
                    assignedTechnician.getId()
            );

            response.setAssignedTechnicianUsername(
                    assignedTechnician.getUsername()
            );
        }

        response.setIssueDescription(
                maintenanceRecord.getIssueDescription()
        );

        response.setReportedDate(
                maintenanceRecord.getReportedDate()
        );

        response.setStartedDate(
                maintenanceRecord.getStartedDate()
        );

        response.setResolvedDate(
                maintenanceRecord.getResolvedDate()
        );

        response.setStatus(
                maintenanceRecord.getStatus()
        );

        response.setResolution(
                maintenanceRecord.getResolution()
        );

        response.setCost(
                maintenanceRecord.getCost()
        );

        response.setCreatedAt(
                maintenanceRecord.getCreatedAt()
        );

        response.setUpdatedAt(
                maintenanceRecord.getUpdatedAt()
        );

        return response;
    }

    private String buildAuditValue(
            MaintenanceRecord maintenanceRecord
    ) {

        String technicianId = "null";

        if (maintenanceRecord.getAssignedTechnician() != null) {
            technicianId =
                    String.valueOf(
                            maintenanceRecord
                                    .getAssignedTechnician()
                                    .getId()
                    );
        }

        String cost = "null";

        if (maintenanceRecord.getCost() != null) {
            cost = maintenanceRecord.getCost().toString();
        }

        return String.format(
                "{\"status\":\"%s\",\"technicianId\":%s,\"cost\":%s}",
                maintenanceRecord.getStatus(),
                technicianId,
                cost
        );
    }

    private String escapeJson(String value) {

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}