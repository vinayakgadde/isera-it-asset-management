package com.isera.assetmanagement.department.service;

import com.isera.assetmanagement.department.dto.DepartmentRequest;
import com.isera.assetmanagement.department.dto.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(
            DepartmentRequest request
    );

    DepartmentResponse getDepartmentById(
            Long id
    );

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse updateDepartment(
            Long id,
            DepartmentRequest request
    );

    void deleteDepartment(
            Long id
    );
}