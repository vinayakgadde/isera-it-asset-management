package com.isera.assetmanagement.location.service.impl;

import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.exception.ResourceNotFoundException;
import com.isera.assetmanagement.location.dto.LocationRequest;
import com.isera.assetmanagement.location.dto.LocationResponse;
import com.isera.assetmanagement.location.entity.Location;
import com.isera.assetmanagement.location.repository.LocationRepository;
import com.isera.assetmanagement.location.service.LocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(
            LocationRepository locationRepository
    ) {
        this.locationRepository = locationRepository;
    }

    @Override
    public LocationResponse createLocation(
            LocationRequest request
    ) {

        if (locationRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException(
                    "Location code already exists: "
                            + request.getCode()
            );
        }

        if (locationRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "Location name already exists: "
                            + request.getName()
            );
        }

        Location location = new Location();

        location.setName(request.getName());
        location.setCode(request.getCode());
        location.setCity(request.getCity());
        location.setState(request.getState());
        location.setCountry(request.getCountry());
        location.setActive(request.getActive());

        Location savedLocation =
                locationRepository.save(location);

        return mapToResponse(savedLocation);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationResponse getLocationById(Long id) {

        Location location = locationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found with id: " + id
                        )
                );

        return mapToResponse(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationResponse> getAllLocations() {

        return locationRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public LocationResponse updateLocation(
            Long id,
            LocationRequest request
    ) {

        Location location = locationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found with id: " + id
                        )
                );

        if (!location.getCode().equals(request.getCode())
                && locationRepository.existsByCode(request.getCode())) {

            throw new DuplicateResourceException(
                    "Location code already exists: "
                            + request.getCode()
            );
        }

        if (!location.getName().equals(request.getName())
                && locationRepository.existsByName(request.getName())) {

            throw new DuplicateResourceException(
                    "Location name already exists: "
                            + request.getName()
            );
        }

        location.setName(request.getName());
        location.setCode(request.getCode());
        location.setCity(request.getCity());
        location.setState(request.getState());
        location.setCountry(request.getCountry());
        location.setActive(request.getActive());

        Location updatedLocation =
                locationRepository.save(location);

        return mapToResponse(updatedLocation);
    }

    @Override
    public void deleteLocation(Long id) {

        Location location = locationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found with id: " + id
                        )
                );

        locationRepository.delete(location);
    }

    private LocationResponse mapToResponse(
            Location location
    ) {

        LocationResponse response =
                new LocationResponse();

        response.setId(location.getId());
        response.setName(location.getName());
        response.setCode(location.getCode());
        response.setCity(location.getCity());
        response.setState(location.getState());
        response.setCountry(location.getCountry());
        response.setActive(location.getActive());
        response.setCreatedAt(location.getCreatedAt());
        response.setUpdatedAt(location.getUpdatedAt());

        return response;
    }
}