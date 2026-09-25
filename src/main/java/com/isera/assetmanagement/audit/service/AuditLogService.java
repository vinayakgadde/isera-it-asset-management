package com.isera.assetmanagement.audit.service;

import com.isera.assetmanagement.audit.dto.AuditLogResponse;

import java.util.List;

public interface AuditLogService {

    void log(
            String action,
            String entityType,
            Long entityId,
            String oldValue,
            String newValue
    );

    List<AuditLogResponse> getAllAuditLogs();

    AuditLogResponse getAuditLogById(Long id);

    List<AuditLogResponse> getAuditLogsByEntity(
            String entityType,
            Long entityId
    );

    List<AuditLogResponse> getAuditLogsByUser(
            Long userId
    );

    List<AuditLogResponse> getAuditLogsByAction(
            String action
    );
}