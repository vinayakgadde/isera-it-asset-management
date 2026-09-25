package com.isera.assetmanagement.employee.controller;

import com.isera.assetmanagement.employee.dto.EmployeeRequest;
import com.isera.assetmanagement.employee.dto.EmployeeResponse;
import com.isera.assetmanagement.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request
    ) {

        EmployeeResponse response =
                employeeService.createEmployee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id
    ) {

        EmployeeResponse response =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {

        return employeeService.getAllEmployees();
    }
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request
    ) {

        EmployeeResponse response =
                employeeService.updateEmployee(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id
    ) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}