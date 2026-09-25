package com.isera.assetmanagement.employee.service;

import com.isera.assetmanagement.employee.dto.EmployeeRequest;
import com.isera.assetmanagement.employee.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request);

    EmployeeResponse getEmployeeById(Long id);

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void deleteEmployee(Long id);
}