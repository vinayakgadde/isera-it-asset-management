package com.isera.assetmanagement.department.service.impl;

import com.isera.assetmanagement.department.dto.DepartmentRequest;
import com.isera.assetmanagement.department.dto.DepartmentResponse;
import com.isera.assetmanagement.department.entity.Department;
import com.isera.assetmanagement.department.repository.DepartmentRepository;
import com.isera.assetmanagement.department.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.exception.ResourceNotFoundException;

import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(
            DepartmentRepository departmentRepository
    ) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentResponse createDepartment(
            DepartmentRequest request
    ) {

        if (departmentRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException(
                    "Department code already exists: "
                            + request.getCode()
            );
        }

        if (departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "Department name already exists: "
                            + request.getName()
            );
        }

        Department department = new Department();

        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setDescription(request.getDescription());
        department.setActive(request.getActive());

        Department savedDepartment =
                departmentRepository.save(department);

        return mapToResponse(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(
            Long id
    ) {

        Department department = departmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: " + id
                        )
                );

        return mapToResponse(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {

        return departmentRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DepartmentResponse updateDepartment(
            Long id,
            DepartmentRequest request
    ) {

        Department department = departmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: " + id
                        )
                );

        if (!department.getCode().equals(request.getCode())
                && departmentRepository.existsByCode(request.getCode())) {

            throw new DuplicateResourceException(
                    "Department code already exists: "
                            + request.getCode()
            );
        }

        if (!department.getName().equals(request.getName())
                && departmentRepository.existsByName(request.getName())) {

            throw new DuplicateResourceException(
                    "Department name already exists: "
                            + request.getName()
            );
        }

        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setDescription(request.getDescription());
        department.setActive(request.getActive());

        Department updatedDepartment =
                departmentRepository.save(department);

        return mapToResponse(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {

        Department department = departmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: " + id
                        )
                );

        departmentRepository.delete(department);
    }

    private DepartmentResponse mapToResponse(
            Department department
    ) {

        DepartmentResponse response =
                new DepartmentResponse();

        response.setId(department.getId());
        response.setName(department.getName());
        response.setCode(department.getCode());
        response.setDescription(department.getDescription());
        response.setActive(department.getActive());
        response.setCreatedAt(department.getCreatedAt());
        response.setUpdatedAt(department.getUpdatedAt());

        return response;
    }
}