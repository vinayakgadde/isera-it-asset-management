package com.isera.assetmanagement.assignment.service.impl;

import com.isera.assetmanagement.asset.entity.Asset;
import com.isera.assetmanagement.asset.repository.AssetRepository;
import com.isera.assetmanagement.assignment.dto.AssetAssignmentRequest;
import com.isera.assetmanagement.assignment.dto.AssetAssignmentResponse;
import com.isera.assetmanagement.assignment.entity.AssetAssignment;
import com.isera.assetmanagement.assignment.repository.AssetAssignmentRepository;
import com.isera.assetmanagement.audit.service.AuditLogService;
import com.isera.assetmanagement.employee.entity.Employee;
import com.isera.assetmanagement.employee.repository.EmployeeRepository;
import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.security.service.CurrentUserService;
import com.isera.assetmanagement.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetAssignmentServiceImplTest {

    @Mock
    private AssetAssignmentRepository assetAssignmentRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AssetAssignmentServiceImpl assetAssignmentService;

    // =========================================================
    // TEST 1
    // Successful asset assignment
    // =========================================================

    @Test
    void shouldAssignAssetSuccessfully() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        AssetAssignmentRequest request =
                new AssetAssignmentRequest();

        request.setAssetId(2L);
        request.setEmployeeId(1L);
        request.setRemarks(
                "Laptop assigned for software development"
        );

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("IN_STOCK");

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setEmployeeCode("EMP001");
        employee.setFirstName("Rahul");
        employee.setLastName("Sharma");
        employee.setStatus("ACTIVE");

        User admin = new User();

        admin.setId(1L);
        admin.setUsername("admin");
        admin.setActive(true);

        when(assetRepository.findById(2L))
                .thenReturn(Optional.of(asset));

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(assetAssignmentRepository
                .findByAssetIdAndStatus(2L, "ACTIVE"))
                .thenReturn(Optional.empty());

        when(currentUserService.getCurrentUser())
                .thenReturn(admin);

        when(assetAssignmentRepository.save(
                any(AssetAssignment.class)
        )).thenAnswer(invocation -> {

            AssetAssignment assignment =
                    invocation.getArgument(0);

            assignment.setId(3L);

            return assignment;
        });

        // -----------------------------------------------------
        // Act
        // -----------------------------------------------------

        AssetAssignmentResponse response =
                assetAssignmentService.createAssignment(
                        request
                );

        // -----------------------------------------------------
        // Assert
        // -----------------------------------------------------

        assertNotNull(response);

        assertEquals(
                3L,
                response.getId()
        );

        assertEquals(
                2L,
                response.getAssetId()
        );

        assertEquals(
                "AST-LAP-001",
                response.getAssetTag()
        );

        assertEquals(
                1L,
                response.getEmployeeId()
        );

        assertEquals(
                "Rahul Sharma",
                response.getEmployeeName()
        );

        assertEquals(
                "ACTIVE",
                response.getStatus()
        );

        assertEquals(
                "Laptop assigned for software development",
                response.getRemarks()
        );

        assertEquals(
                "ASSIGNED",
                asset.getStatus()
        );

        // -----------------------------------------------------
        // Verify repository calls
        // -----------------------------------------------------

        verify(assetRepository)
                .findById(2L);

        verify(employeeRepository)
                .findById(1L);

        verify(assetAssignmentRepository)
                .findByAssetIdAndStatus(
                        2L,
                        "ACTIVE"
                );

        verify(assetAssignmentRepository)
                .save(any(AssetAssignment.class));

        verify(assetRepository)
                .save(asset);

        // -----------------------------------------------------
        // Verify audit logging
        // -----------------------------------------------------

        verify(auditLogService).log(
                eq("ASSET_ASSIGNED"),
                eq("ASSET_ASSIGNMENT"),
                eq(3L),
                eq("{\"assetStatus\":\"IN_STOCK\"}"),
                eq("{\"assetStatus\":\"ASSIGNED\",\"employeeId\":1,\"assignmentId\":3}")
        );
    }

    // =========================================================
    // TEST 2
    // Reject assignment when asset is not IN_STOCK
    // =========================================================

    @Test
    void shouldRejectAssignmentWhenAssetIsNotInStock() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        AssetAssignmentRequest request =
                new AssetAssignmentRequest();

        request.setAssetId(2L);
        request.setEmployeeId(1L);
        request.setRemarks(
                "Laptop assigned for software development"
        );

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("ASSIGNED");

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setEmployeeCode("EMP001");
        employee.setFirstName("Rahul");
        employee.setLastName("Sharma");
        employee.setStatus("ACTIVE");

        when(assetRepository.findById(2L))
                .thenReturn(Optional.of(asset));

        // IMPORTANT:
        // The service looks up the employee before
        // checking the asset status.
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        // -----------------------------------------------------
        // Act + Assert
        // -----------------------------------------------------

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () ->
                                assetAssignmentService
                                        .createAssignment(request)
                );

        assertEquals(
                "Asset cannot be assigned because current status is: ASSIGNED",
                exception.getMessage()
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(assetRepository)
                .findById(2L);

        verify(employeeRepository)
                .findById(1L);

        // The service should stop before checking
        // active assignment because asset is already ASSIGNED.
        verify(assetAssignmentRepository, never())
                .findByAssetIdAndStatus(
                        anyLong(),
                        anyString()
                );

        verify(assetAssignmentRepository, never())
                .save(any(AssetAssignment.class));

        verify(assetRepository, never())
                .save(any(Asset.class));

        verify(currentUserService, never())
                .getCurrentUser();

        verify(auditLogService, never())
                .log(
                        anyString(),
                        anyString(),
                        anyLong(),
                        anyString(),
                        anyString()
                );
    }

    // =========================================================
// TEST 3
// Successful asset return
// =========================================================

    @Test
    void shouldReturnAssetSuccessfully() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("ASSIGNED");

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setEmployeeCode("EMP001");
        employee.setFirstName("Rahul");
        employee.setLastName("Sharma");
        employee.setStatus("ACTIVE");

        User admin = new User();

        admin.setId(1L);
        admin.setUsername("admin");
        admin.setActive(true);

        AssetAssignment assignment =
                new AssetAssignment();

        assignment.setId(3L);
        assignment.setAsset(asset);
        assignment.setEmployee(employee);
        assignment.setAssignedBy(admin);
        assignment.setAssignedDate(
                java.time.LocalDateTime.now().minusMinutes(10)
        );
        assignment.setStatus("ACTIVE");
        assignment.setRemarks(
                "Laptop assigned for software development"
        );

        when(assetAssignmentRepository.findById(3L))
                .thenReturn(Optional.of(assignment));

        when(currentUserService.getCurrentUser())
                .thenReturn(admin);

        when(assetAssignmentRepository.save(
                any(AssetAssignment.class)
        )).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        // -----------------------------------------------------
        // Act
        // -----------------------------------------------------

        AssetAssignmentResponse response =
                assetAssignmentService.returnAsset(3L);

        // -----------------------------------------------------
        // Assert
        // -----------------------------------------------------

        assertNotNull(response);

        assertEquals(
                3L,
                response.getId()
        );

        assertEquals(
                2L,
                response.getAssetId()
        );

        assertEquals(
                "AST-LAP-001",
                response.getAssetTag()
        );

        assertEquals(
                1L,
                response.getEmployeeId()
        );

        assertEquals(
                "Rahul Sharma",
                response.getEmployeeName()
        );

        assertEquals(
                "RETURNED",
                response.getStatus()
        );

        assertNotNull(
                response.getReturnedDate()
        );

        assertEquals(
                1L,
                response.getReturnedById()
        );

        assertEquals(
                "admin",
                response.getReturnedByName()
        );

        // Asset must become IN_STOCK
        assertEquals(
                "IN_STOCK",
                asset.getStatus()
        );

        // -----------------------------------------------------
        // Verify repository calls
        // -----------------------------------------------------

        verify(assetAssignmentRepository)
                .findById(3L);

        verify(currentUserService)
                .getCurrentUser();

        verify(assetAssignmentRepository)
                .save(assignment);

        verify(assetRepository)
                .save(asset);

        // -----------------------------------------------------
        // Verify audit logging
        // -----------------------------------------------------

        verify(auditLogService).log(
                eq("ASSET_RETURNED"),
                eq("ASSET_ASSIGNMENT"),
                eq(3L),
                eq("{\"assetStatus\":\"ASSIGNED\",\"assignmentStatus\":\"ACTIVE\",\"employeeId\":1}"),
                eq("{\"assetStatus\":\"IN_STOCK\",\"assignmentStatus\":\"RETURNED\",\"employeeId\":1}")
        );
    }

    // =========================================================
// TEST 4
// Reject return when assignment is already RETURNED
// =========================================================

    @Test
    void shouldRejectReturnWhenAssignmentIsAlreadyReturned() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("IN_STOCK");

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setEmployeeCode("EMP001");
        employee.setFirstName("Rahul");
        employee.setLastName("Sharma");
        employee.setStatus("ACTIVE");

        User admin = new User();

        admin.setId(1L);
        admin.setUsername("admin");
        admin.setActive(true);

        AssetAssignment assignment =
                new AssetAssignment();

        assignment.setId(3L);
        assignment.setAsset(asset);
        assignment.setEmployee(employee);
        assignment.setAssignedBy(admin);
        assignment.setAssignedDate(
                java.time.LocalDateTime.now().minusMinutes(20)
        );
        assignment.setReturnedBy(admin);
        assignment.setReturnedDate(
                java.time.LocalDateTime.now().minusMinutes(10)
        );
        assignment.setStatus("RETURNED");
        assignment.setRemarks(
                "Laptop assigned for software development"
        );

        when(assetAssignmentRepository.findById(3L))
                .thenReturn(Optional.of(assignment));

        // -----------------------------------------------------
        // Act + Assert
        // -----------------------------------------------------

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () ->
                                assetAssignmentService.returnAsset(3L)
                );

        assertEquals(
                "Asset assignment is already returned",
                exception.getMessage()
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(assetAssignmentRepository)
                .findById(3L);

        verify(currentUserService, never())
                .getCurrentUser();

        verify(assetAssignmentRepository, never())
                .save(any(AssetAssignment.class));

        verify(assetRepository, never())
                .save(any(Asset.class));

        verify(auditLogService, never())
                .log(
                        anyString(),
                        anyString(),
                        anyLong(),
                        anyString(),
                        anyString()
                );
    }

}