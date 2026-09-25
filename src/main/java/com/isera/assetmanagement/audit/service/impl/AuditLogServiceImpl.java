package com.isera.assetmanagement.audit.service.impl;

import com.isera.assetmanagement.audit.dto.AuditLogResponse;
import com.isera.assetmanagement.audit.entity.AuditLog;
import com.isera.assetmanagement.audit.repository.AuditLogRepository;
import com.isera.assetmanagement.audit.service.AuditLogService;
import com.isera.assetmanagement.security.service.CurrentUserService;
import com.isera.assetmanagement.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final CurrentUserService currentUserService;
    private final HttpServletRequest request;

    public AuditLogServiceImpl(
            AuditLogRepository auditLogRepository,
            CurrentUserService currentUserService,
            HttpServletRequest request
    ) {
        this.auditLogRepository = auditLogRepository;
        this.currentUserService = currentUserService;
        this.request = request;
    }

    @Override
    @Transactional
    public void log(
            String action,
            String entityType,
            Long entityId,
            String oldValue,
            String newValue
    ) {

        User currentUser = currentUserService.getCurrentUser();

        AuditLog auditLog = new AuditLog();

        auditLog.setUser(currentUser);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setOldValue(oldValue);
        auditLog.setNewValue(newValue);
        auditLog.setIpAddress(getClientIpAddress());

        auditLogRepository.save(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAllAuditLogs() {

        return auditLogRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogResponse getAuditLogById(Long id) {

        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() ->
                        new com.isera.assetmanagement.exception.ResourceNotFoundException(
                                "Audit log not found with id: " + id
                        )
                );

        return mapToResponse(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogsByEntity(
            String entityType,
            Long entityId
    ) {

        return auditLogRepository
                .findByEntityTypeAndEntityIdOrderByCreatedAtDesc(
                        entityType,
                        entityId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogsByUser(
            Long userId
    ) {

        return auditLogRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogsByAction(
            String action
    ) {

        return auditLogRepository
                .findByActionOrderByCreatedAtDesc(action)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AuditLogResponse mapToResponse(AuditLog auditLog) {

        AuditLogResponse response = new AuditLogResponse();

        response.setId(auditLog.getId());

        if (auditLog.getUser() != null) {
            response.setUserId(auditLog.getUser().getId());
            response.setUsername(auditLog.getUser().getUsername());
        }

        response.setAction(auditLog.getAction());
        response.setEntityType(auditLog.getEntityType());
        response.setEntityId(auditLog.getEntityId());
        response.setOldValue(auditLog.getOldValue());
        response.setNewValue(auditLog.getNewValue());
        response.setIpAddress(auditLog.getIpAddress());
        response.setCreatedAt(auditLog.getCreatedAt());

        return response;
    }

    private String getClientIpAddress() {

        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");

        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }

        return request.getRemoteAddr();
    }
}