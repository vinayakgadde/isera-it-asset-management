package com.isera.assetmanagement.audit.repository;

import com.isera.assetmanagement.audit.entity.AuditLog;
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
class AuditLogRepositoryTest {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Test
    void shouldFindAllAuditLogs() {

        List<AuditLog> result =
                auditLogRepository.findAll();

        assertNotNull(result);

        assertFalse(
                result.isEmpty(),
                "Audit logs should exist in the database"
        );
    }

    @Test
    void shouldFindAuditLogById() {

        List<AuditLog> logs =
                auditLogRepository.findAll();

        assertFalse(logs.isEmpty());

        Long id = logs.get(0).getId();

        Optional<AuditLog> result =
                auditLogRepository.findById(id);

        assertTrue(result.isPresent());

        assertEquals(
                id,
                result.get().getId()
        );
    }

    @Test
    void shouldHaveValidAuditAction() {

        List<AuditLog> logs =
                auditLogRepository.findAll();

        assertFalse(logs.isEmpty());

        AuditLog auditLog = logs.get(0);

        assertNotNull(
                auditLog.getAction()
        );

        assertFalse(
                auditLog.getAction().isBlank()
        );
    }

    @Test
    void shouldHaveValidEntityType() {

        List<AuditLog> logs =
                auditLogRepository.findAll();

        assertFalse(logs.isEmpty());

        AuditLog auditLog = logs.get(0);

        assertNotNull(
                auditLog.getEntityType()
        );

        assertFalse(
                auditLog.getEntityType().isBlank()
        );
    }

    @Test
    void shouldReturnEmptyWhenAuditLogDoesNotExist() {

        Optional<AuditLog> result =
                auditLogRepository.findById(999999L);

        assertTrue(
                result.isEmpty()
        );
    }
}