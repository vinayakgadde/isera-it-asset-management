package com.isera.assetmanagement.dashboard.service.impl;

import com.isera.assetmanagement.asset.entity.Asset;
import com.isera.assetmanagement.asset.repository.AssetRepository;
import com.isera.assetmanagement.assignment.entity.AssetAssignment;
import com.isera.assetmanagement.assignment.repository.AssetAssignmentRepository;
import com.isera.assetmanagement.dashboard.dto.DashboardAssetActivityResponse;
import com.isera.assetmanagement.dashboard.dto.DashboardSummaryResponse;
import com.isera.assetmanagement.dashboard.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final int RECENT_ACTIVITY_LIMIT = 5;

    private static final int WARRANTY_EXPIRY_DAYS = 30;

    private final AssetRepository assetRepository;

    private final AssetAssignmentRepository assetAssignmentRepository;

    public DashboardServiceImpl(
            AssetRepository assetRepository,
            AssetAssignmentRepository assetAssignmentRepository
    ) {
        this.assetRepository = assetRepository;
        this.assetAssignmentRepository =
                assetAssignmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryResponse getDashboardSummary() {

        List<Asset> assets =
                assetRepository.findAll();

        List<AssetAssignment> assignments =
                assetAssignmentRepository.findAll();

        DashboardSummaryResponse response =
                new DashboardSummaryResponse();

        response.setTotalAssets(
                (long) assets.size()
        );

        response.setAssetsByStatus(
                getAssetsByStatus(assets)
        );

        response.setAssetsByCategory(
                getAssetsByCategory(assets)
        );

        response.setAssignedAssetsByDepartment(
                getAssignedAssetsByDepartment(assignments)
        );

        response.setAssetsUnderMaintenance(
                getAssetsUnderMaintenance(assets)
        );

        response.setWarrantyExpiringAssets(
                getWarrantyExpiringAssets(assets)
        );

        response.setRecentlyAssignedAssets(
                getRecentlyAssignedAssets(assignments)
        );

        response.setRecentlyReturnedAssets(
                getRecentlyReturnedAssets(assignments)
        );

        return response;
    }

    private Map<String, Long> getAssetsByStatus(
            List<Asset> assets
    ) {

        return assets.stream()
                .filter(asset ->
                        asset.getStatus() != null
                )
                .collect(
                        Collectors.groupingBy(
                                Asset::getStatus,
                                LinkedHashMap::new,
                                Collectors.counting()
                        )
                );
    }

    private Map<String, Long> getAssetsByCategory(
            List<Asset> assets
    ) {

        return assets.stream()
                .filter(asset ->
                        asset.getCategory() != null
                )
                .filter(asset ->
                        asset.getCategory().getName() != null
                )
                .collect(
                        Collectors.groupingBy(
                                asset ->
                                        asset.getCategory().getName(),
                                LinkedHashMap::new,
                                Collectors.counting()
                        )
                );
    }

    private Map<String, Long>
    getAssignedAssetsByDepartment(
            List<AssetAssignment> assignments
    ) {

        return assignments.stream()
                .filter(assignment ->
                        "ACTIVE".equals(
                                assignment.getStatus()
                        )
                )
                .filter(assignment ->
                        assignment.getEmployee() != null
                )
                .filter(assignment ->
                        assignment.getEmployee()
                                .getDepartment() != null
                )
                .filter(assignment ->
                        assignment.getEmployee()
                                .getDepartment()
                                .getName() != null
                )
                .collect(
                        Collectors.groupingBy(
                                assignment ->
                                        assignment
                                                .getEmployee()
                                                .getDepartment()
                                                .getName(),
                                LinkedHashMap::new,
                                Collectors.counting()
                        )
                );
    }

    private long getAssetsUnderMaintenance(
            List<Asset> assets
    ) {

        return assets.stream()
                .filter(asset ->
                        "UNDER_MAINTENANCE".equals(
                                asset.getStatus()
                        )
                )
                .count();
    }

    private long getWarrantyExpiringAssets(
            List<Asset> assets
    ) {

        LocalDate today =
                LocalDate.now();

        LocalDate expiryLimit =
                today.plusDays(
                        WARRANTY_EXPIRY_DAYS
                );

        return assets.stream()
                .filter(asset ->
                        asset.getWarrantyExpiryDate() != null
                )
                .filter(asset ->
                        !asset.getWarrantyExpiryDate()
                                .isBefore(today)
                )
                .filter(asset ->
                        !asset.getWarrantyExpiryDate()
                                .isAfter(expiryLimit)
                )
                .count();
    }

    private List<DashboardAssetActivityResponse>
    getRecentlyAssignedAssets(
            List<AssetAssignment> assignments
    ) {

        return assignments.stream()
                .filter(assignment ->
                        assignment.getAssignedDate() != null
                )
                .sorted(
                        Comparator.comparing(
                                AssetAssignment::getAssignedDate
                        ).reversed()
                )
                .limit(RECENT_ACTIVITY_LIMIT)
                .map(this::mapToActivityResponse)
                .toList();
    }

    private List<DashboardAssetActivityResponse>
    getRecentlyReturnedAssets(
            List<AssetAssignment> assignments
    ) {

        return assignments.stream()
                .filter(assignment ->
                        assignment.getReturnedDate() != null
                )
                .sorted(
                        Comparator.comparing(
                                AssetAssignment::getReturnedDate
                        ).reversed()
                )
                .limit(RECENT_ACTIVITY_LIMIT)
                .map(this::mapToActivityResponse)
                .toList();
    }

    private DashboardAssetActivityResponse
    mapToActivityResponse(
            AssetAssignment assignment
    ) {

        DashboardAssetActivityResponse response =
                new DashboardAssetActivityResponse();

        response.setAssignmentId(
                assignment.getId()
        );

        if (assignment.getAsset() != null) {

            response.setAssetId(
                    assignment.getAsset().getId()
            );

            response.setAssetTag(
                    assignment.getAsset().getAssetTag()
            );
        }

        if (assignment.getEmployee() != null) {

            response.setEmployeeId(
                    assignment.getEmployee().getId()
            );

            response.setEmployeeName(
                    assignment.getEmployee().getFirstName()
                            + " "
                            + assignment.getEmployee().getLastName()
            );
        }

        response.setAssignedDate(
                assignment.getAssignedDate()
        );

        response.setReturnedDate(
                assignment.getReturnedDate()
        );

        response.setStatus(
                assignment.getStatus()
        );

        return response;
    }
}