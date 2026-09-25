package com.isera.assetmanagement.vendor.controller;

import com.isera.assetmanagement.vendor.dto.VendorRequest;
import com.isera.assetmanagement.vendor.dto.VendorResponse;
import com.isera.assetmanagement.vendor.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public ResponseEntity<VendorResponse> createVendor(
            @Valid @RequestBody VendorRequest request) {

        VendorResponse response =
                vendorService.createVendor(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorResponse> getVendorById(
            @PathVariable Long id) {

        VendorResponse response =
                vendorService.getVendorById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VendorResponse>> getAllVendors() {

        List<VendorResponse> response =
                vendorService.getAllVendors();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorResponse> updateVendor(
            @PathVariable Long id,
            @Valid @RequestBody VendorRequest request) {

        VendorResponse response =
                vendorService.updateVendor(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(
            @PathVariable Long id) {

        vendorService.deleteVendor(id);

        return ResponseEntity.noContent().build();
    }
}