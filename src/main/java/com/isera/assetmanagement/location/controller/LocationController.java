package com.isera.assetmanagement.location.controller;

import com.isera.assetmanagement.location.dto.LocationRequest;
import com.isera.assetmanagement.location.dto.LocationResponse;
import com.isera.assetmanagement.location.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(
            LocationService locationService
    ) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(
            @Valid @RequestBody LocationRequest request
    ) {

        LocationResponse response =
                locationService.createLocation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(
            @PathVariable Long id
    ) {

        LocationResponse response =
                locationService.getLocationById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {

        List<LocationResponse> response =
                locationService.getAllLocations();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequest request
    ) {

        LocationResponse response =
                locationService.updateLocation(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable Long id
    ) {

        locationService.deleteLocation(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}