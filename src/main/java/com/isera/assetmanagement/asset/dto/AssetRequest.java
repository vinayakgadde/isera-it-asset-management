package com.isera.assetmanagement.asset.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AssetRequest {

    @NotBlank(message = "Asset tag is required")
    @Size(max = 50, message = "Asset tag must not exceed 50 characters")
    private String assetTag;

    @Size(max = 100, message = "Serial number must not exceed 100 characters")
    private String serialNumber;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Long vendorId;

    @NotBlank(message = "Brand is required")
    @Size(max = 100, message = "Brand must not exceed 100 characters")
    private String brand;

    @Size(max = 150, message = "Model must not exceed 150 characters")
    private String model;

    private LocalDate purchaseDate;

    @DecimalMin(value = "0.0", message = "Purchase cost cannot be negative")
    private BigDecimal purchaseCost;

    private LocalDate warrantyExpiryDate;

    @NotNull(message = "Location ID is required")
    private Long locationId;

    @NotBlank(message = "Status is required")
    @Size(max = 30, message = "Status must not exceed 30 characters")
    private String status = "IN_STOCK";

    @NotBlank(message = "Asset condition is required")
    @Size(max = 30, message = "Asset condition must not exceed 30 characters")
    private String condition = "GOOD";

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;


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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
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
}