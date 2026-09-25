package com.isera.assetmanagement.maintenance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class MaintenanceRecordRequest {

    @NotNull
    @Positive
    private Long assetId;

    @Positive
    private Long reportedByEmployeeId;

    @Positive
    private Long assignedTechnicianId;

    @NotBlank
    @Size(max = 1000)
    private String issueDescription;

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getReportedByEmployeeId() {
        return reportedByEmployeeId;
    }

    public void setReportedByEmployeeId(Long reportedByEmployeeId) {
        this.reportedByEmployeeId = reportedByEmployeeId;
    }

    public Long getAssignedTechnicianId() {
        return assignedTechnicianId;
    }

    public void setAssignedTechnicianId(Long assignedTechnicianId) {
        this.assignedTechnicianId = assignedTechnicianId;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }
}