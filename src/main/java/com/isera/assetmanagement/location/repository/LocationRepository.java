package com.isera.assetmanagement.location.repository;

import com.isera.assetmanagement.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository
        extends JpaRepository<Location, Long> {

    Optional<Location> findByCode(String code);

    Optional<Location> findByName(String name);

    boolean existsByCode(String code);

    boolean existsByName(String name);
}