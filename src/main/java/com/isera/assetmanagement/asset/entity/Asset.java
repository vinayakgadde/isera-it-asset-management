package com.isera.assetmanagement.asset.entity;

import com.isera.assetmanagement.category.entity.AssetCategory;
import com.isera.assetmanagement.location.entity.Location;
import com.isera.assetmanagement.vendor.entity.Vendor;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "assets",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_assets_asset_tag",
                        columnNames = "asset_tag"
                ),
                @UniqueConstraint(
                        name = "uk_assets_serial_number",
                        columnNames = "serial_number"
                )
        }
)
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "asset_tag",
            nullable = false,
            length = 50
    )
    private String assetTag;

    @Column(
            name = "serial_number",
            length = 100
    )
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_assets_category")
    )
    private AssetCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "vendor_id",
            foreignKey = @ForeignKey(name = "fk_assets_vendor")
    )
    private Vendor vendor;

    @Column(
            name = "brand",
            nullable = false,
            length = 100
    )
    private String brand;

    @Column(
            name = "model",
            length = 150
    )
    private String model;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(
            name = "purchase_cost",
            precision = 12,
            scale = 2
    )
    private BigDecimal purchaseCost;

    @Column(name = "warranty_expiry_date")
    private LocalDate warrantyExpiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "location_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_assets_location")
    )
    private Location location;

    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private String status = "IN_STOCK";

    @Column(
            name = "asset_condition",
            nullable = false,
            length = 30
    )
    private String condition = "GOOD";

    @Column(
            name = "description",
            length = 500
    )
    private String description;

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

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public AssetCategory getCategory() {
        return category;
    }

    public void setCategory(AssetCategory category) {
        this.category = category;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public LocalDate getWarrantyExpiryDate() {
        return warrantyExpiryDate;
    }

    public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) {
        this.warrantyExpiryDate = warrantyExpiryDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}