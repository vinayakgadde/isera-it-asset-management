package com.isera.assetmanagement.user.repository;

import com.isera.assetmanagement.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findById(Long id);

    @EntityGraph(attributePaths = "roles")
    List<User> findAll();

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);
}