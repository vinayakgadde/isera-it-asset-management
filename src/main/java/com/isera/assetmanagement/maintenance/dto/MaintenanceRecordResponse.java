package com.isera.assetmanagement.maintenance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MaintenanceRecordResponse {

    private Long id;

    private Long assetId;
    private String assetTag;

    private Long reportedByEmployeeId;
    private String reportedByEmployeeName;

    private Long assignedTechnicianId;
    private String assignedTechnicianUsername;

    private String issueDescription;

    private LocalDateTime reportedDate;
    private LocalDateTime startedDate;
    private LocalDateTime resolvedDate;

    private String status;

    private String resolution;

    private BigDecimal cost;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public Long getReportedByEmployeeId() {
        return reportedByEmployeeId;
    }

    public void setReportedByEmployeeId(Long reportedByEmployeeId) {
        this.reportedByEmployeeId = reportedByEmployeeId;
    }

    public String getReportedByEmployeeName() {
        return reportedByEmployeeName;
    }

    public void setReportedByEmployeeName(String reportedByEmployeeName) {
        this.reportedByEmployeeName = reportedByEmployeeName;
    }

    public Long getAssignedTechnicianId() {
        return assignedTechnicianId;
    }

    public void setAssignedTechnicianId(Long assignedTechnicianId) {
        this.assignedTechnicianId = assignedTechnicianId;
    }

    public String getAssignedTechnicianUsername() {
        return assignedTechnicianUsername;
    }

    public void setAssignedTechnicianUsername(String assignedTechnicianUsername) {
        this.assignedTechnicianUsername = assignedTechnicianUsername;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public LocalDateTime getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDateTime reportedDate) {
        this.reportedDate = reportedDate;
    }

    public LocalDateTime getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDateTime startedDate) {
        this.startedDate = startedDate;
    }

    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}