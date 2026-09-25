package com.isera.assetmanagement.assignment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AssetAssignmentRequest {

    @NotNull(message = "Asset ID is required")
    private Long assetId;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @Size(
            max = 500,
            message = "Remarks must not exceed 500 characters"
    )
    private String remarks;


    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}