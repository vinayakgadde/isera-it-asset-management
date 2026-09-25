package com.isera.assetmanagement.assignment.service.impl;

import com.isera.assetmanagement.asset.entity.Asset;
import com.isera.assetmanagement.asset.repository.AssetRepository;
import com.isera.assetmanagement.assignment.dto.AssetAssignmentRequest;
import com.isera.assetmanagement.assignment.dto.AssetAssignmentResponse;
import com.isera.assetmanagement.assignment.entity.AssetAssignment;
import com.isera.assetmanagement.assignment.repository.AssetAssignmentRepository;
import com.isera.assetmanagement.assignment.service.AssetAssignmentService;
import com.isera.assetmanagement.audit.service.AuditLogService;
import com.isera.assetmanagement.employee.entity.Employee;
import com.isera.assetmanagement.employee.repository.EmployeeRepository;
import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.exception.ResourceNotFoundException;
import com.isera.assetmanagement.security.service.CurrentUserService;
import com.isera.assetmanagement.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssetAssignmentServiceImpl implements AssetAssignmentService {

    private final AssetAssignmentRepository assetAssignmentRepository;
    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;
    private final CurrentUserService currentUserService;
    private final AuditLogService auditLogService;

    public AssetAssignmentServiceImpl(
            AssetAssignmentRepository assetAssignmentRepository,
            AssetRepository assetRepository,
            EmployeeRepository employeeRepository,
            CurrentUserService currentUserService,
            AuditLogService auditLogService
    ) {
        this.assetAssignmentRepository = assetAssignmentRepository;
        this.assetRepository = assetRepository;
        this.employeeRepository = employeeRepository;
        this.currentUserService = currentUserService;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional
    public AssetAssignmentResponse createAssignment(
            AssetAssignmentRequest request
    ) {

        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Asset not found with id: " + request.getAssetId()
                        )
                );

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with id: " + request.getEmployeeId()
                        )
                );

        if (!"IN_STOCK".equals(asset.getStatus())) {

            throw new DuplicateResourceException(
                    "Asset cannot be assigned because current status is: "
                            + asset.getStatus()
            );
        }

        boolean activeAssignmentExists =
                assetAssignmentRepository
                        .findByAssetIdAndStatus(
                                asset.getId(),
                                "ACTIVE"
                        )
                        .isPresent();

        if (activeAssignmentExists) {

            throw new DuplicateResourceException(
                    "Asset is already assigned"
            );
        }

        User currentUser = currentUserService.getCurrentUser();

        AssetAssignment assignment = new AssetAssignment();

        assignment.setAsset(asset);
        assignment.setEmployee(employee);
        assignment.setAssignedBy(currentUser);
        assignment.setAssignedDate(LocalDateTime.now());
        assignment.setStatus("ACTIVE");
        assignment.setRemarks(request.getRemarks());

        AssetAssignment savedAssignment =
                assetAssignmentRepository.save(assignment);

        String oldValue = String.format(
                "{\"assetStatus\":\"%s\"}",
                asset.getStatus()
        );

        asset.setStatus("ASSIGNED");

        assetRepository.save(asset);

        String newValue = String.format(
                "{\"assetStatus\":\"ASSIGNED\",\"employeeId\":%d,\"assignmentId\":%d}",
                employee.getId(),
                savedAssignment.getId()
        );

        auditLogService.log(
                "ASSET_ASSIGNED",
                "ASSET_ASSIGNMENT",
                savedAssignment.getId(),
                oldValue,
                newValue
        );

        return mapToResponse(savedAssignment);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetAssignmentResponse getAssignmentById(Long id) {

        AssetAssignment assignment =
                assetAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asset assignment not found with id: " + id
                                )
                        );

        return mapToResponse(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetAssignmentResponse> getAllAssignments() {

        return assetAssignmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public AssetAssignmentResponse returnAsset(Long id) {

        AssetAssignment assignment =
                assetAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asset assignment not found with id: " + id
                                )
                        );

        if (!"ACTIVE".equals(assignment.getStatus())) {

            throw new DuplicateResourceException(
                    "Asset assignment is already returned"
            );
        }

        User currentUser = currentUserService.getCurrentUser();

        Asset asset = assignment.getAsset();

        String oldValue = String.format(
                "{\"assetStatus\":\"%s\",\"assignmentStatus\":\"%s\",\"employeeId\":%d}",
                asset.getStatus(),
                assignment.getStatus(),
                assignment.getEmployee().getId()
        );

        assignment.setReturnedBy(currentUser);
        assignment.setReturnedDate(LocalDateTime.now());
        assignment.setStatus("RETURNED");

        asset.setStatus("IN_STOCK");

        assetRepository.save(asset);

        AssetAssignment savedAssignment =
                assetAssignmentRepository.save(assignment);

        String newValue = String.format(
                "{\"assetStatus\":\"IN_STOCK\",\"assignmentStatus\":\"RETURNED\",\"employeeId\":%d}",
                assignment.getEmployee().getId()
        );

        auditLogService.log(
                "ASSET_RETURNED",
                "ASSET_ASSIGNMENT",
                savedAssignment.getId(),
                oldValue,
                newValue
        );

        return mapToResponse(savedAssignment);
    }

    @Override
    @Transactional
    public void deleteAssignment(Long id) {

        AssetAssignment assignment =
                assetAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Asset assignment not found with id: " + id
                                )
                        );

        String oldValue = String.format(
                "{\"assetId\":%d,\"employeeId\":%d,\"status\":\"%s\"}",
                assignment.getAsset().getId(),
                assignment.getEmployee().getId(),
                assignment.getStatus()
        );

        auditLogService.log(
                "ASSET_ASSIGNMENT_DELETED",
                "ASSET_ASSIGNMENT",
                assignment.getId(),
                oldValue,
                null
        );

        assetAssignmentRepository.delete(assignment);
    }

    private AssetAssignmentResponse mapToResponse(
            AssetAssignment assignment
    ) {

        AssetAssignmentResponse response =
                new AssetAssignmentResponse();

        response.setId(assignment.getId());

        response.setAssetId(
                assignment.getAsset().getId()
        );

        response.setAssetTag(
                assignment.getAsset().getAssetTag()
        );

        response.setEmployeeId(
                assignment.getEmployee().getId()
        );

        response.setEmployeeName(
                assignment.getEmployee().getFirstName()
                        + " "
                        + assignment.getEmployee().getLastName()
        );

        response.setAssignedById(
                assignment.getAssignedBy() != null
                        ? assignment.getAssignedBy().getId()
                        : null
        );

        response.setAssignedByName(
                assignment.getAssignedBy() != null
                        ? assignment.getAssignedBy().getUsername()
                        : null
        );

        response.setReturnedById(
                assignment.getReturnedBy() != null
                        ? assignment.getReturnedBy().getId()
                        : null
        );

        response.setReturnedByName(
                assignment.getReturnedBy() != null
                        ? assignment.getReturnedBy().getUsername()
                        : null
        );

        response.setAssignedDate(
                assignment.getAssignedDate()
        );

        response.setReturnedDate(
                assignment.getReturnedDate()
        );

        response.setStatus(
                assignment.getStatus()
        );

        response.setRemarks(
                assignment.getRemarks()
        );

        response.setCreatedAt(
                assignment.getCreatedAt()
        );

        response.setUpdatedAt(
                assignment.getUpdatedAt()
        );

        return response;
    }
}