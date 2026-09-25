package com.isera.assetmanagement.assignment.entity;

import com.isera.assetmanagement.asset.entity.Asset;
import com.isera.assetmanagement.employee.entity.Employee;
import com.isera.assetmanagement.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "asset_assignments")
public class AssetAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "asset_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_assignment_asset")
    )
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employee_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_assignment_employee")
    )
    private Employee employee;

    @Column(
            name = "assigned_date",
            nullable = false
    )
    private LocalDateTime assignedDate;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "assigned_by",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_assignment_assigned_by")
    )
    private User assignedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "returned_by",
            foreignKey = @ForeignKey(name = "fk_assignment_returned_by")
    )
    private User returnedBy;

    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private String status = "ACTIVE";

    @Column(
            name = "remarks",
            length = 500
    )
    private String remarks;

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

        if (assignedDate == null) {
            assignedDate = now;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
    }

    public User getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(User assignedBy) {
        this.assignedBy = assignedBy;
    }

    public User getReturnedBy() {
        return returnedBy;
    }

    public void setReturnedBy(User returnedBy) {
        this.returnedBy = returnedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}