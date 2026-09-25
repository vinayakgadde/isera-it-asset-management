package com.isera.assetmanagement.maintenance.service.impl;

import com.isera.assetmanagement.asset.entity.Asset;
import com.isera.assetmanagement.asset.repository.AssetRepository;
import com.isera.assetmanagement.assignment.entity.AssetAssignment;
import com.isera.assetmanagement.assignment.repository.AssetAssignmentRepository;
import com.isera.assetmanagement.audit.service.AuditLogService;
import com.isera.assetmanagement.employee.entity.Employee;
import com.isera.assetmanagement.employee.repository.EmployeeRepository;
import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordRequest;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordResponse;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordUpdateRequest;
import com.isera.assetmanagement.maintenance.entity.MaintenanceRecord;
import com.isera.assetmanagement.maintenance.repository.MaintenanceRecordRepository;
import com.isera.assetmanagement.user.entity.User;
import com.isera.assetmanagement.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaintenanceRecordServiceImplTest {

    @Mock
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssetAssignmentRepository assetAssignmentRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private MaintenanceRecordServiceImpl maintenanceRecordService;

    // =========================================================
    // TEST 1
    // Create maintenance successfully
    // =========================================================

    @Test
    void shouldCreateMaintenanceSuccessfully() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        MaintenanceRecordRequest request =
                new MaintenanceRecordRequest();

        request.setAssetId(2L);
        request.setReportedByEmployeeId(1L);
        request.setAssignedTechnicianId(1L);
        request.setIssueDescription(
                "Laptop is overheating during normal usage"
        );

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("IN_STOCK");

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("Rahul");
        employee.setLastName("Sharma");
        employee.setStatus("ACTIVE");

        User technician = new User();

        technician.setId(1L);
        technician.setUsername("admin");
        technician.setActive(true);

        when(assetRepository.findById(2L))
                .thenReturn(Optional.of(asset));

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "OPEN"))
                .thenReturn(java.util.List.of());

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "IN_PROGRESS"))
                .thenReturn(java.util.List.of());

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(technician));

        when(maintenanceRecordRepository.save(
                any(MaintenanceRecord.class)
        )).thenAnswer(invocation -> {

            MaintenanceRecord record =
                    invocation.getArgument(0);

            record.setId(1L);

            return record;
        });

        // -----------------------------------------------------
        // Act
        // -----------------------------------------------------

        MaintenanceRecordResponse response =
                maintenanceRecordService.createMaintenance(
                        request
                );

        // -----------------------------------------------------
        // Assert
        // -----------------------------------------------------

        assertNotNull(response);

        assertEquals(
                1L,
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
                response.getReportedByEmployeeId()
        );

        assertEquals(
                "Rahul Sharma",
                response.getReportedByEmployeeName()
        );

        assertEquals(
                1L,
                response.getAssignedTechnicianId()
        );

        assertEquals(
                "admin",
                response.getAssignedTechnicianUsername()
        );

        assertEquals(
                "Laptop is overheating during normal usage",
                response.getIssueDescription()
        );

        assertEquals(
                "OPEN",
                response.getStatus()
        );

        // Asset must move into maintenance
        assertEquals(
                "UNDER_MAINTENANCE",
                asset.getStatus()
        );

        // -----------------------------------------------------
        // Verify persistence
        // -----------------------------------------------------

        verify(assetRepository)
                .findById(2L);

        verify(employeeRepository)
                .findById(1L);

        verify(userRepository)
                .findById(1L);

        verify(maintenanceRecordRepository)
                .save(any(MaintenanceRecord.class));

        verify(assetRepository)
                .save(asset);

        // -----------------------------------------------------
        // Verify audit
        // -----------------------------------------------------

        verify(auditLogService).log(
                eq("MAINTENANCE_CREATED"),
                eq("MAINTENANCE_RECORD"),
                eq(1L),
                eq("{\"assetStatus\":\"IN_STOCK\"}"),
                anyString()
        );
    }

    // =========================================================
    // TEST 2
    // Start maintenance successfully
    // =========================================================

    @Test
    void shouldStartMaintenanceSuccessfully() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        MaintenanceRecord record =
                createMaintenanceRecord(
                        1L,
                        "OPEN"
                );

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        when(maintenanceRecordRepository.save(
                any(MaintenanceRecord.class)
        )).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        // -----------------------------------------------------
        // Act
        // -----------------------------------------------------

        MaintenanceRecordResponse response =
                maintenanceRecordService.startMaintenance(1L);

        // -----------------------------------------------------
        // Assert
        // -----------------------------------------------------

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "IN_PROGRESS",
                response.getStatus()
        );

        assertNotNull(
                response.getStartedDate()
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(maintenanceRecordRepository)
                .findById(1L);

        verify(maintenanceRecordRepository)
                .save(record);

        verify(auditLogService).log(
                eq("MAINTENANCE_STARTED"),
                eq("MAINTENANCE_RECORD"),
                eq(1L),
                eq("{\"status\":\"OPEN\"}"),
                anyString()
        );
    }

    // =========================================================
    // TEST 3
    // Resolve maintenance successfully
    // =========================================================

    @Test
    void shouldResolveMaintenanceSuccessfully() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        MaintenanceRecord record =
                createMaintenanceRecord(
                        1L,
                        "IN_PROGRESS"
                );

        MaintenanceRecordUpdateRequest request =
                new MaintenanceRecordUpdateRequest();

        request.setResolution(
                "Cleaned cooling fan and replaced thermal paste"
        );

        request.setCost(
                new BigDecimal("1500.00")
        );

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        when(maintenanceRecordRepository.save(
                any(MaintenanceRecord.class)
        )).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        // -----------------------------------------------------
        // Act
        // -----------------------------------------------------

        MaintenanceRecordResponse response =
                maintenanceRecordService.resolveMaintenance(
                        1L,
                        request
                );

        // -----------------------------------------------------
        // Assert
        // -----------------------------------------------------

        assertNotNull(response);

        assertEquals(
                "RESOLVED",
                response.getStatus()
        );

        assertEquals(
                "Cleaned cooling fan and replaced thermal paste",
                response.getResolution()
        );

        assertEquals(
                new BigDecimal("1500.00"),
                response.getCost()
        );

        assertNotNull(
                response.getResolvedDate()
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(maintenanceRecordRepository)
                .findById(1L);

        verify(maintenanceRecordRepository)
                .save(record);

        verify(auditLogService).log(
                eq("MAINTENANCE_RESOLVED"),
                eq("MAINTENANCE_RECORD"),
                eq(1L),
                eq("{\"status\":\"IN_PROGRESS\"}"),
                anyString()
        );
    }

    // =========================================================
    // TEST 4
    // Close maintenance successfully
    // =========================================================

    @Test
    void shouldCloseMaintenanceSuccessfully() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("UNDER_MAINTENANCE");

        MaintenanceRecord record =
                createMaintenanceRecord(
                        1L,
                        "RESOLVED"
                );

        record.setAsset(asset);
        record.setResolvedDate(
                LocalDateTime.now()
        );
        record.setResolution(
                "Cleaned cooling fan and replaced thermal paste"
        );
        record.setCost(
                new BigDecimal("1500.00")
        );

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        // No active assignment exists
        when(assetAssignmentRepository
                .findByAssetIdAndStatus(
                        2L,
                        "ACTIVE"
                ))
                .thenReturn(Optional.empty());

        when(maintenanceRecordRepository.save(
                any(MaintenanceRecord.class)
        )).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        // -----------------------------------------------------
        // Act
        // -----------------------------------------------------

        MaintenanceRecordResponse response =
                maintenanceRecordService.closeMaintenance(1L);

        // -----------------------------------------------------
        // Assert
        // -----------------------------------------------------

        assertNotNull(response);

        assertEquals(
                "CLOSED",
                response.getStatus()
        );

        // Asset should return to IN_STOCK
        assertEquals(
                "IN_STOCK",
                asset.getStatus()
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(maintenanceRecordRepository)
                .findById(1L);

        verify(assetAssignmentRepository)
                .findByAssetIdAndStatus(
                        2L,
                        "ACTIVE"
                );

        verify(assetRepository)
                .save(asset);

        verify(maintenanceRecordRepository)
                .save(record);

        verify(auditLogService).log(
                eq("MAINTENANCE_CLOSED"),
                eq("MAINTENANCE_RECORD"),
                eq(1L),
                anyString(),
                eq("{\"maintenanceStatus\":\"CLOSED\",\"assetStatus\":\"IN_STOCK\"}")
        );
    }

    // =========================================================
    // TEST 5
    // Reject starting maintenance when status is not OPEN
    // =========================================================

    @Test
    void shouldRejectStartWhenMaintenanceIsNotOpen() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        MaintenanceRecord record =
                createMaintenanceRecord(
                        1L,
                        "RESOLVED"
                );

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        // -----------------------------------------------------
        // Act + Assert
        // -----------------------------------------------------

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () ->
                                maintenanceRecordService
                                        .startMaintenance(1L)
                );

        assertEquals(
                "Maintenance can only be started when status is OPEN",
                exception.getMessage()
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(maintenanceRecordRepository)
                .findById(1L);

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));

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
    // Helper method
    // =========================================================

    private MaintenanceRecord createMaintenanceRecord(
            Long id,
            String status
    ) {

        MaintenanceRecord record =
                new MaintenanceRecord();

        record.setId(id);

        record.setStatus(status);

        record.setIssueDescription(
                "Laptop is overheating during normal usage"
        );

        record.setReportedDate(
                LocalDateTime.now().minusHours(1)
        );

        record.setCreatedAt(
                LocalDateTime.now().minusHours(1)
        );

        record.setUpdatedAt(
                LocalDateTime.now().minusMinutes(30)
        );

        return record;
    }


    @Test
    void shouldRejectResolveWhenMaintenanceIsNotInProgress() {

        MaintenanceRecord record = new MaintenanceRecord();
        record.setId(1L);
        record.setStatus("OPEN");

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        assertThrows(
                DuplicateResourceException.class,
                () -> maintenanceRecordService.resolveMaintenance(
                        1L,
                        new MaintenanceRecordUpdateRequest()
                )
        );

        verify(maintenanceRecordRepository, never()).save(any(MaintenanceRecord.class));
        verify(auditLogService, never()).log(
                anyString(),
                anyString(),
                anyLong(),
                anyString(),
                anyString()
        );
    }

    @Test
    void shouldRejectCloseWhenMaintenanceIsNotResolved() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("UNDER_MAINTENANCE");

        MaintenanceRecord record =
                createMaintenanceRecord(
                        1L,
                        "IN_PROGRESS"
                );

        record.setAsset(asset);

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        // -----------------------------------------------------
        // Act + Assert
        // -----------------------------------------------------

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> maintenanceRecordService.closeMaintenance(1L)
                );

        assertEquals(
                "Maintenance can only be closed when status is RESOLVED",
                exception.getMessage()
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(maintenanceRecordRepository)
                .findById(1L);

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));

        verify(assetRepository, never())
                .save(any(Asset.class));

        verify(assetAssignmentRepository, never())
                .findByAssetIdAndStatus(anyLong(), anyString());

        verify(auditLogService, never())
                .log(
                        anyString(),
                        anyString(),
                        anyLong(),
                        anyString(),
                        anyString()
                );
    }

    @Test
    void shouldRejectResolveWhenResolutionIsMissing() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        MaintenanceRecord record =
                createMaintenanceRecord(
                        1L,
                        "IN_PROGRESS"
                );

        MaintenanceRecordUpdateRequest request =
                new MaintenanceRecordUpdateRequest();

        request.setResolution("");

        request.setCost(
                new BigDecimal("1500.00")
        );

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        // -----------------------------------------------------
        // Act + Assert
        // -----------------------------------------------------

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> maintenanceRecordService.resolveMaintenance(
                                1L,
                                request
                        )
                );

        assertEquals(
                "Resolution is required to resolve maintenance",
                exception.getMessage()
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(maintenanceRecordRepository)
                .findById(1L);

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));

        verify(auditLogService, never())
                .log(
                        anyString(),
                        anyString(),
                        anyLong(),
                        anyString(),
                        anyString()
                );
    }

    @Test
    void shouldKeepAssetAssignedWhenClosingMaintenanceWithActiveAssignment() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("UNDER_MAINTENANCE");

        MaintenanceRecord record =
                createMaintenanceRecord(
                        1L,
                        "RESOLVED"
                );

        record.setAsset(asset);

        record.setResolvedDate(
                LocalDateTime.now()
        );

        record.setResolution(
                "Cleaned cooling fan and replaced thermal paste"
        );

        record.setCost(
                new BigDecimal("1500.00")
        );

        AssetAssignment activeAssignment =
                new AssetAssignment();

        activeAssignment.setId(3L);

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        when(assetAssignmentRepository.findByAssetIdAndStatus(
                2L,
                "ACTIVE"
        )).thenReturn(Optional.of(activeAssignment));

        when(maintenanceRecordRepository.save(
                any(MaintenanceRecord.class)
        )).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        // -----------------------------------------------------
        // Act
        // -----------------------------------------------------

        MaintenanceRecordResponse response =
                maintenanceRecordService.closeMaintenance(1L);

        // -----------------------------------------------------
        // Assert
        // -----------------------------------------------------

        assertNotNull(response);

        assertEquals(
                "CLOSED",
                response.getStatus()
        );

        // Asset must remain assigned
        assertEquals(
                "ASSIGNED",
                asset.getStatus()
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(maintenanceRecordRepository)
                .findById(1L);

        verify(assetAssignmentRepository)
                .findByAssetIdAndStatus(
                        2L,
                        "ACTIVE"
                );

        verify(assetRepository)
                .save(asset);

        verify(maintenanceRecordRepository)
                .save(record);

        verify(auditLogService).log(
                eq("MAINTENANCE_CLOSED"),
                eq("MAINTENANCE_RECORD"),
                eq(1L),
                anyString(),
                eq("{\"maintenanceStatus\":\"CLOSED\",\"assetStatus\":\"ASSIGNED\"}")
        );
    }

    @Test
    void shouldRejectUpdateWhenMaintenanceIsClosed() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        MaintenanceRecord record =
                createMaintenanceRecord(
                        1L,
                        "CLOSED"
                );

        MaintenanceRecordUpdateRequest request =
                new MaintenanceRecordUpdateRequest();

        request.setResolution(
                "Trying to modify closed maintenance"
        );

        request.setCost(
                new BigDecimal("2000.00")
        );

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        // -----------------------------------------------------
        // Act + Assert
        // -----------------------------------------------------

        assertThrows(
                DuplicateResourceException.class,
                () -> maintenanceRecordService.updateMaintenance(
                        1L,
                        request
                )
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(maintenanceRecordRepository)
                .findById(1L);

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));

        verify(auditLogService, never())
                .log(
                        anyString(),
                        anyString(),
                        anyLong(),
                        anyString(),
                        anyString()
                );
    }

    @Test
    void shouldRejectCreateWhenOpenMaintenanceAlreadyExists() {

        // -----------------------------------------------------
        // Arrange
        // -----------------------------------------------------

        MaintenanceRecordRequest request =
                new MaintenanceRecordRequest();

        request.setAssetId(2L);

        request.setIssueDescription(
                "Laptop has another issue"
        );

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("IN_STOCK");

        MaintenanceRecord existingMaintenance =
                new MaintenanceRecord();

        existingMaintenance.setId(10L);
        existingMaintenance.setStatus("OPEN");

        when(assetRepository.findById(2L))
                .thenReturn(Optional.of(asset));

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "OPEN"))
                .thenReturn(java.util.List.of(existingMaintenance));

        // -----------------------------------------------------
        // Act + Assert
        // -----------------------------------------------------

        assertThrows(
                DuplicateResourceException.class,
                () -> maintenanceRecordService.createMaintenance(request)
        );

        // -----------------------------------------------------
        // Verify
        // -----------------------------------------------------

        verify(assetRepository)
                .findById(2L);

        verify(maintenanceRecordRepository)
                .findByAssetIdAndStatus(2L, "OPEN");

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));

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

    @Test
    void shouldRejectCreateWhenInProgressMaintenanceAlreadyExists() {

        MaintenanceRecordRequest request =
                new MaintenanceRecordRequest();

        request.setAssetId(2L);
        request.setIssueDescription(
                "Laptop has another issue"
        );

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("IN_STOCK");

        MaintenanceRecord existingMaintenance =
                new MaintenanceRecord();

        existingMaintenance.setId(10L);
        existingMaintenance.setStatus("IN_PROGRESS");

        when(assetRepository.findById(2L))
                .thenReturn(Optional.of(asset));

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "OPEN"))
                .thenReturn(java.util.List.of());

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "IN_PROGRESS"))
                .thenReturn(java.util.List.of(existingMaintenance));

        assertThrows(
                DuplicateResourceException.class,
                () -> maintenanceRecordService.createMaintenance(request)
        );

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));

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

    @Test
    void shouldRejectCreateForRetiredAsset() {

        MaintenanceRecordRequest request =
                new MaintenanceRecordRequest();

        request.setAssetId(2L);
        request.setIssueDescription(
                "Retired laptop requires maintenance"
        );

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("RETIRED");

        when(assetRepository.findById(2L))
                .thenReturn(Optional.of(asset));

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "OPEN"))
                .thenReturn(java.util.List.of());

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "IN_PROGRESS"))
                .thenReturn(java.util.List.of());

        assertThrows(
                DuplicateResourceException.class,
                () -> maintenanceRecordService.createMaintenance(request)
        );

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));

        verify(assetRepository, never())
                .save(any(Asset.class));
    }

    @Test
    void shouldRejectCreateForDisposedAsset() {

        MaintenanceRecordRequest request =
                new MaintenanceRecordRequest();

        request.setAssetId(2L);
        request.setIssueDescription(
                "Disposed laptop requires maintenance"
        );

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("DISPOSED");

        when(assetRepository.findById(2L))
                .thenReturn(Optional.of(asset));

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "OPEN"))
                .thenReturn(java.util.List.of());

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "IN_PROGRESS"))
                .thenReturn(java.util.List.of());

        assertThrows(
                DuplicateResourceException.class,
                () -> maintenanceRecordService.createMaintenance(request)
        );

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));

        verify(assetRepository, never())
                .save(any(Asset.class));
    }

    @Test
    void shouldRejectCreateWhenReportingEmployeeIsInactive() {

        MaintenanceRecordRequest request =
                new MaintenanceRecordRequest();

        request.setAssetId(2L);
        request.setReportedByEmployeeId(1L);
        request.setIssueDescription(
                "Laptop is damaged"
        );

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("IN_STOCK");

        Employee employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("Rahul");
        employee.setLastName("Sharma");
        employee.setStatus("INACTIVE");

        when(assetRepository.findById(2L))
                .thenReturn(Optional.of(asset));

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "OPEN"))
                .thenReturn(java.util.List.of());

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "IN_PROGRESS"))
                .thenReturn(java.util.List.of());

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        assertThrows(
                DuplicateResourceException.class,
                () -> maintenanceRecordService.createMaintenance(request)
        );

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));

        verify(assetRepository, never())
                .save(any(Asset.class));
    }

    @Test
    void shouldRejectCreateWhenTechnicianIsInactive() {

        MaintenanceRecordRequest request =
                new MaintenanceRecordRequest();

        request.setAssetId(2L);
        request.setAssignedTechnicianId(1L);
        request.setIssueDescription(
                "Laptop is damaged"
        );

        Asset asset = new Asset();

        asset.setId(2L);
        asset.setAssetTag("AST-LAP-001");
        asset.setStatus("IN_STOCK");

        User technician = new User();

        technician.setId(1L);
        technician.setUsername("employee1");
        technician.setActive(false);

        when(assetRepository.findById(2L))
                .thenReturn(Optional.of(asset));

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "OPEN"))
                .thenReturn(java.util.List.of());

        when(maintenanceRecordRepository
                .findByAssetIdAndStatus(2L, "IN_PROGRESS"))
                .thenReturn(java.util.List.of());

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(technician));

        assertThrows(
                DuplicateResourceException.class,
                () -> maintenanceRecordService.createMaintenance(request)
        );

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));

        verify(assetRepository, never())
                .save(any(Asset.class));
    }

    @Test
    void shouldUpdateMaintenanceSuccessfully() {

        MaintenanceRecord record =
                createMaintenanceRecord(
                        1L,
                        "IN_PROGRESS"
                );

        MaintenanceRecordUpdateRequest request =
                new MaintenanceRecordUpdateRequest();

        request.setResolution(
                "Replaced damaged cooling fan"
        );

        request.setCost(
                new BigDecimal("2000.00")
        );

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        when(maintenanceRecordRepository.save(
                any(MaintenanceRecord.class)
        )).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        MaintenanceRecordResponse response =
                maintenanceRecordService.updateMaintenance(
                        1L,
                        request
                );

        assertNotNull(response);

        assertEquals(
                "Replaced damaged cooling fan",
                response.getResolution()
        );

        assertEquals(
                new BigDecimal("2000.00"),
                response.getCost()
        );

        verify(maintenanceRecordRepository)
                .findById(1L);

        verify(maintenanceRecordRepository)
                .save(record);

        verify(auditLogService).log(
                eq("MAINTENANCE_UPDATED"),
                eq("MAINTENANCE_RECORD"),
                eq(1L),
                anyString(),
                anyString()
        );
    }

    @Test
    void shouldRejectUpdateWhenTechnicianDoesNotExist() {

        MaintenanceRecord record =
                createMaintenanceRecord(
                        1L,
                        "IN_PROGRESS"
                );

        MaintenanceRecordUpdateRequest request =
                new MaintenanceRecordUpdateRequest();

        request.setAssignedTechnicianId(99L);

        when(maintenanceRecordRepository.findById(1L))
                .thenReturn(Optional.of(record));

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> maintenanceRecordService.updateMaintenance(
                        1L,
                        request
                )
        );

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));
    }

    @Test
    void shouldRejectStartWhenMaintenanceDoesNotExist() {

        when(maintenanceRecordRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> maintenanceRecordService.startMaintenance(999L)
        );

        verify(maintenanceRecordRepository)
                .findById(999L);

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));
    }

    @Test
    void shouldRejectUpdateWhenMaintenanceDoesNotExist() {

        when(maintenanceRecordRepository.findById(999L))
                .thenReturn(Optional.empty());

        MaintenanceRecordUpdateRequest request =
                new MaintenanceRecordUpdateRequest();

        request.setResolution(
                "Updated resolution"
        );

        assertThrows(
                RuntimeException.class,
                () -> maintenanceRecordService.updateMaintenance(
                        999L,
                        request
                )
        );

        verify(maintenanceRecordRepository)
                .findById(999L);

        verify(maintenanceRecordRepository, never())
                .save(any(MaintenanceRecord.class));
    }

}