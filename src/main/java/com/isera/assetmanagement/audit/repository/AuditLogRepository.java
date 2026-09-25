package com.isera.assetmanagement.audit.repository;

import com.isera.assetmanagement.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    @EntityGraph(attributePaths = "user")
    List<AuditLog> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "user")
    Optional<AuditLog> findById(Long id);

    @EntityGraph(attributePaths = "user")
    List<AuditLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = "user")
    List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(
            String entityType,
            Long entityId
    );

    @EntityGraph(attributePaths = "user")
    List<AuditLog> findByActionOrderByCreatedAtDesc(
            String action
    );
}