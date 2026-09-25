package com.isera.assetmanagement.location.service;

import com.isera.assetmanagement.location.dto.LocationRequest;
import com.isera.assetmanagement.location.dto.LocationResponse;

import java.util.List;

public interface LocationService {

    LocationResponse createLocation(
            LocationRequest request
    );

    LocationResponse getLocationById(
            Long id
    );

    List<LocationResponse> getAllLocations();

    LocationResponse updateLocation(
            Long id,
            LocationRequest request
    );

    void deleteLocation(
            Long id
    );
}