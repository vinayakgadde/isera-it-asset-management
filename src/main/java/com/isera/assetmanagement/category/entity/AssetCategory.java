package com.isera.assetmanagement.category.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "asset_categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_asset_categories_name",
                        columnNames = "name"
                ),
                @UniqueConstraint(
                        name = "uk_asset_categories_code",
                        columnNames = "code"
                )
        }
)
public class AssetCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;

    @Column(
            name = "code",
            nullable = false,
            length = 30
    )
    private String code;

    @Column(
            name = "description",
            length = 255
    )
    private String description;

    @Column(
            name = "is_active",
            nullable = false
    )
    private Boolean active = true;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}