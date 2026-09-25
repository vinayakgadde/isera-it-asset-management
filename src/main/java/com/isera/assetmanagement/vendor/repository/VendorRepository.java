package com.isera.assetmanagement.vendor.repository;

import com.isera.assetmanagement.vendor.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository
        extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByCode(String code);

    Optional<Vendor> findByName(String name);

    boolean existsByCode(String code);

    boolean existsByName(String name);
}