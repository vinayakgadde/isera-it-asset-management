package com.isera.assetmanagement.dashboard.dto;

import java.util.List;
import java.util.Map;

public class DashboardSummaryResponse {

    private Long totalAssets;

    private Map<String, Long> assetsByStatus;

    private Map<String, Long> assetsByCategory;

    private Map<String, Long> assignedAssetsByDepartment;

    private Long assetsUnderMaintenance;

    private Long warrantyExpiringAssets;

    private List<DashboardAssetActivityResponse> recentlyAssignedAssets;

    private List<DashboardAssetActivityResponse> recentlyReturnedAssets;

    public Long getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(Long totalAssets) {
        this.totalAssets = totalAssets;
    }

    public Map<String, Long> getAssetsByStatus() {
        return assetsByStatus;
    }

    public void setAssetsByStatus(
            Map<String, Long> assetsByStatus
    ) {
        this.assetsByStatus = assetsByStatus;
    }

    public Map<String, Long> getAssetsByCategory() {
        return assetsByCategory;
    }

    public void setAssetsByCategory(
            Map<String, Long> assetsByCategory
    ) {
        this.assetsByCategory = assetsByCategory;
    }

    public Map<String, Long> getAssignedAssetsByDepartment() {
        return assignedAssetsByDepartment;
    }

    public void setAssignedAssetsByDepartment(
            Map<String, Long> assignedAssetsByDepartment
    ) {
        this.assignedAssetsByDepartment =
                assignedAssetsByDepartment;
    }

    public Long getAssetsUnderMaintenance() {
        return assetsUnderMaintenance;
    }

    public void setAssetsUnderMaintenance(
            Long assetsUnderMaintenance
    ) {
        this.assetsUnderMaintenance =
                assetsUnderMaintenance;
    }

    public Long getWarrantyExpiringAssets() {
        return warrantyExpiringAssets;
    }

    public void setWarrantyExpiringAssets(
            Long warrantyExpiringAssets
    ) {
        this.warrantyExpiringAssets =
                warrantyExpiringAssets;
    }

    public List<DashboardAssetActivityResponse>
    getRecentlyAssignedAssets() {
        return recentlyAssignedAssets;
    }

    public void setRecentlyAssignedAssets(
            List<DashboardAssetActivityResponse>
                    recentlyAssignedAssets
    ) {
        this.recentlyAssignedAssets =
                recentlyAssignedAssets;
    }

    public List<DashboardAssetActivityResponse>
    getRecentlyReturnedAssets() {
        return recentlyReturnedAssets;
    }

    public void setRecentlyReturnedAssets(
            List<DashboardAssetActivityResponse>
                    recentlyReturnedAssets
    ) {
        this.recentlyReturnedAssets =
                recentlyReturnedAssets;
    }
}