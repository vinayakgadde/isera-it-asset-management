package com.isera.assetmanagement.audit.controller;

import com.isera.assetmanagement.audit.dto.AuditLogResponse;
import com.isera.assetmanagement.audit.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(
            AuditLogService auditLogService
    ) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAllAuditLogs() {

        return ResponseEntity.ok(
                auditLogService.getAllAuditLogs()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponse> getAuditLogById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogById(id)
        );
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogsByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId
    ) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByEntity(
                        entityType,
                        entityId
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogsByUser(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByUser(userId)
        );
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogsByAction(
            @PathVariable String action
    ) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByAction(action)
        );
    }
}