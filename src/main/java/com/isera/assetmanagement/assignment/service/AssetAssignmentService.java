package com.isera.assetmanagement.assignment.service;

import com.isera.assetmanagement.assignment.dto.AssetAssignmentRequest;
import com.isera.assetmanagement.assignment.dto.AssetAssignmentResponse;

import java.util.List;

public interface AssetAssignmentService {

    AssetAssignmentResponse createAssignment(
            AssetAssignmentRequest request
    );

    AssetAssignmentResponse getAssignmentById(
            Long id
    );

    List<AssetAssignmentResponse> getAllAssignments();

    AssetAssignmentResponse returnAsset(
            Long assignmentId
    );

    void deleteAssignment(Long id);
}