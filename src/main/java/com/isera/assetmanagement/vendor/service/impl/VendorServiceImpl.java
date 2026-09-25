package com.isera.assetmanagement.vendor.service.impl;

import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.exception.ResourceNotFoundException;
import com.isera.assetmanagement.vendor.dto.VendorRequest;
import com.isera.assetmanagement.vendor.dto.VendorResponse;
import com.isera.assetmanagement.vendor.entity.Vendor;
import com.isera.assetmanagement.vendor.repository.VendorRepository;
import com.isera.assetmanagement.vendor.service.VendorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public VendorResponse createVendor(VendorRequest request) {

        if (vendorRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException(
                    "Vendor code already exists: " + request.getCode()
            );
        }

        if (vendorRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "Vendor name already exists: " + request.getName()
            );
        }

        Vendor vendor = new Vendor();

        vendor.setName(request.getName());
        vendor.setCode(request.getCode());
        vendor.setContactPerson(request.getContactPerson());
        vendor.setEmail(request.getEmail());
        vendor.setPhone(request.getPhone());
        vendor.setAddress(request.getAddress());
        vendor.setActive(request.getActive());

        Vendor savedVendor = vendorRepository.save(vendor);

        return mapToResponse(savedVendor);
    }

    @Override
    public VendorResponse getVendorById(Long id) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor not found with id: " + id
                ));

        return mapToResponse(vendor);
    }

    @Override
    public List<VendorResponse> getAllVendors() {

        return vendorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public VendorResponse updateVendor(
            Long id,
            VendorRequest request
    ) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor not found with id: " + id
                ));

        if (!vendor.getCode().equals(request.getCode())
                && vendorRepository.existsByCode(request.getCode())) {

            throw new DuplicateResourceException(
                    "Vendor code already exists: " + request.getCode()
            );
        }

        if (!vendor.getName().equals(request.getName())
                && vendorRepository.existsByName(request.getName())) {

            throw new DuplicateResourceException(
                    "Vendor name already exists: " + request.getName()
            );
        }

        vendor.setName(request.getName());
        vendor.setCode(request.getCode());
        vendor.setContactPerson(request.getContactPerson());
        vendor.setEmail(request.getEmail());
        vendor.setPhone(request.getPhone());
        vendor.setAddress(request.getAddress());
        vendor.setActive(request.getActive());

        Vendor updatedVendor = vendorRepository.save(vendor);

        return mapToResponse(updatedVendor);
    }

    @Override
    public void deleteVendor(Long id) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor not found with id: " + id
                ));

        vendorRepository.delete(vendor);
    }

    private VendorResponse mapToResponse(Vendor vendor) {

        VendorResponse response = new VendorResponse();

        response.setId(vendor.getId());
        response.setName(vendor.getName());
        response.setCode(vendor.getCode());
        response.setContactPerson(vendor.getContactPerson());
        response.setEmail(vendor.getEmail());
        response.setPhone(vendor.getPhone());
        response.setAddress(vendor.getAddress());
        response.setActive(vendor.getActive());
        response.setCreatedAt(vendor.getCreatedAt());
        response.setUpdatedAt(vendor.getUpdatedAt());

        return response;
    }
}