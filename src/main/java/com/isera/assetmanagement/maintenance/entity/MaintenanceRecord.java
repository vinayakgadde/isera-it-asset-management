package com.isera.assetmanagement.maintenance.entity;

import com.isera.assetmanagement.asset.entity.Asset;
import com.isera.assetmanagement.employee.entity.Employee;
import com.isera.assetmanagement.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "asset_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_maintenance_asset")
    )
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "reported_by",
            foreignKey = @ForeignKey(name = "fk_maintenance_reported_by")
    )
    private Employee reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "assigned_technician",
            foreignKey = @ForeignKey(name = "fk_maintenance_technician")
    )
    private User assignedTechnician;

    @Column(
            name = "issue_description",
            nullable = false,
            length = 1000
    )
    private String issueDescription;

    @Column(
            name = "reported_date",
            nullable = false
    )
    private LocalDateTime reportedDate;

    @Column(name = "started_date")
    private LocalDateTime startedDate;

    @Column(name = "resolved_date")
    private LocalDateTime resolvedDate;

    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private String status = "OPEN";

    @Column(
            name = "resolution",
            length = 1000
    )
    private String resolution;

    @Column(
            name = "cost",
            precision = 12,
            scale = 2
    )
    private BigDecimal cost;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        if (reportedDate == null) {
            reportedDate = now;
        }

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Employee getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(Employee reportedBy) {
        this.reportedBy = reportedBy;
    }

    public User getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(User assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
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