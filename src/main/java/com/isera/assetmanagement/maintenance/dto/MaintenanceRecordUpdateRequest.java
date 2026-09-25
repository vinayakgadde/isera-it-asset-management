package com.isera.assetmanagement.maintenance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class MaintenanceRecordUpdateRequest {

    @Size(max = 1000)
    private String resolution;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal cost;

    private Long assignedTechnicianId;

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

    public Long getAssignedTechnicianId() {
        return assignedTechnicianId;
    }

    public void setAssignedTechnicianId(Long assignedTechnicianId) {
        this.assignedTechnicianId = assignedTechnicianId;
    }
}