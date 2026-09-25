package com.isera.assetmanagement.maintenance.repository;

import com.isera.assetmanagement.maintenance.entity.MaintenanceRecord;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class MaintenanceRecordRepositoryTest {

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    // =========================================================
    // TEST 1
    // Find maintenance records by asset ID
    // =========================================================

    @Test
    void shouldFindMaintenanceRecordsByAssetId() {

        List<MaintenanceRecord> records =
                maintenanceRecordRepository.findByAssetId(2L);

        assertNotNull(records);

        assertFalse(
                records.isEmpty()
        );

        assertTrue(
                records.stream()
                        .allMatch(record ->
                                record.getAsset().getId().equals(2L)
                        )
        );
    }

    // =========================================================
    // TEST 2
    // Find maintenance records by employee who reported them
    // =========================================================

    @Test
    void shouldFindMaintenanceRecordsByReportedEmployeeId() {

        List<MaintenanceRecord> records =
                maintenanceRecordRepository
                        .findByReportedById(1L);

        assertNotNull(records);

        assertFalse(
                records.isEmpty()
        );

        assertTrue(
                records.stream()
                        .allMatch(record ->
                                record.getReportedBy() != null
                                        && record.getReportedBy()
                                        .getId()
                                        .equals(1L)
                        )
        );
    }

    // =========================================================
    // TEST 3
    // Find maintenance records by assigned technician
    // =========================================================

    @Test
    void shouldFindMaintenanceRecordsByAssignedTechnicianId() {

        List<MaintenanceRecord> records =
                maintenanceRecordRepository
                        .findByAssignedTechnicianId(1L);

        assertNotNull(records);

        assertFalse(
                records.isEmpty()
        );

        assertTrue(
                records.stream()
                        .allMatch(record ->
                                record.getAssignedTechnician() != null
                                        && record.getAssignedTechnician()
                                        .getId()
                                        .equals(1L)
                        )
        );
    }

    // =========================================================
    // TEST 4
    // Find maintenance records by status
    // =========================================================

    @Test
    void shouldFindClosedMaintenanceRecordsByStatus() {

        List<MaintenanceRecord> records =
                maintenanceRecordRepository
                        .findByStatus("CLOSED");

        assertNotNull(records);

        assertFalse(
                records.isEmpty()
        );

        assertTrue(
                records.stream()
                        .allMatch(record ->
                                "CLOSED".equals(
                                        record.getStatus()
                                )
                        )
        );
    }

    // =========================================================
    // TEST 5
    // Find maintenance records by asset ID and status
    // =========================================================

    @Test
    void shouldFindClosedMaintenanceRecordsByAssetAndStatus() {

        List<MaintenanceRecord> records =
                maintenanceRecordRepository
                        .findByAssetIdAndStatus(
                                2L,
                                "CLOSED"
                        );

        assertNotNull(records);

        assertFalse(
                records.isEmpty()
        );

        assertTrue(
                records.stream()
                        .allMatch(record ->
                                record.getAsset().getId().equals(2L)
                                        && "CLOSED".equals(
                                        record.getStatus()
                                )
                        )
        );
    }

    // =========================================================
    // TEST 6
    // Unknown asset should return empty list
    // =========================================================

    @Test
    void shouldReturnEmptyWhenAssetDoesNotExist() {

        List<MaintenanceRecord> records =
                maintenanceRecordRepository
                        .findByAssetId(999999L);

        assertNotNull(records);

        assertTrue(
                records.isEmpty()
        );
    }

    // =========================================================
    // TEST 7
    // Unknown employee should return empty list
    // =========================================================

    @Test
    void shouldReturnEmptyWhenReportedEmployeeDoesNotExist() {

        List<MaintenanceRecord> records =
                maintenanceRecordRepository
                        .findByReportedById(999999L);

        assertNotNull(records);

        assertTrue(
                records.isEmpty()
        );
    }

    // =========================================================
    // TEST 8
    // Unknown status should return empty list
    // =========================================================

    @Test
    void shouldReturnEmptyForUnknownMaintenanceStatus() {

        List<MaintenanceRecord> records =
                maintenanceRecordRepository
                        .findByStatus("STATUS_DOES_NOT_EXIST");

        assertNotNull(records);

        assertTrue(
                records.isEmpty()
        );
    }
}