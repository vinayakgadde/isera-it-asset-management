package com.isera.assetmanagement.employee.repository;

import com.isera.assetmanagement.employee.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    boolean existsByEmployeeCode(
            String employeeCode
    );

    boolean existsByEmail(
            String email
    );

    @EntityGraph(
            attributePaths = {
                    "department",
                    "location"
            }
    )
    List<Employee> findAll();

    @EntityGraph(
            attributePaths = {
                    "department",
                    "location"
            }
    )
    Optional<Employee> findById(Long id);

    Optional<Employee> findByUserId(
            Long userId
    );
}