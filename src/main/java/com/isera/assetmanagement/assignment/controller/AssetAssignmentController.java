package com.isera.assetmanagement.assignment.controller;

import com.isera.assetmanagement.assignment.dto.AssetAssignmentRequest;
import com.isera.assetmanagement.assignment.dto.AssetAssignmentResponse;
import com.isera.assetmanagement.assignment.service.AssetAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asset-assignments")
public class AssetAssignmentController {

    private final AssetAssignmentService assignmentService;

    public AssetAssignmentController(
            AssetAssignmentService assignmentService
    ) {
        this.assignmentService =
                assignmentService;
    }

    @PostMapping
    public ResponseEntity<AssetAssignmentResponse>
    createAssignment(
            @Valid @RequestBody
            AssetAssignmentRequest request
    ) {

        AssetAssignmentResponse response =
                assignmentService.createAssignment(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetAssignmentResponse>
    getAssignmentById(
            @PathVariable Long id
    ) {

        AssetAssignmentResponse response =
                assignmentService.getAssignmentById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AssetAssignmentResponse>>
    getAllAssignments() {

        List<AssetAssignmentResponse> response =
                assignmentService.getAllAssignments();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<AssetAssignmentResponse>
    returnAsset(
            @PathVariable Long id
    ) {

        AssetAssignmentResponse response =
                assignmentService.returnAsset(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable Long id
    ) {

        assignmentService.deleteAssignment(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}