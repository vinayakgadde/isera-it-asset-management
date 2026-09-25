package com.isera.assetmanagement.employee.service.impl;

import com.isera.assetmanagement.department.entity.Department;
import com.isera.assetmanagement.department.repository.DepartmentRepository;
import com.isera.assetmanagement.employee.dto.EmployeeRequest;
import com.isera.assetmanagement.employee.dto.EmployeeResponse;
import com.isera.assetmanagement.employee.entity.Employee;
import com.isera.assetmanagement.employee.repository.EmployeeRepository;
import com.isera.assetmanagement.employee.service.EmployeeService;
import com.isera.assetmanagement.exception.DuplicateResourceException;
import com.isera.assetmanagement.exception.ResourceNotFoundException;
import com.isera.assetmanagement.location.entity.Location;
import com.isera.assetmanagement.location.repository.LocationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final LocationRepository locationRepository;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            LocationRepository locationRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmployeeCode(
                request.getEmployeeCode())) {

            throw new DuplicateResourceException(
                    "Employee code already exists: "
                            + request.getEmployeeCode()
            );
        }

        if (employeeRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Employee email already exists: "
                            + request.getEmail()
            );
        }

        Department department = departmentRepository.findById(
                request.getDepartmentId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "Department not found with id: "
                        + request.getDepartmentId()
        ));

        Location location = locationRepository.findById(
                request.getLocationId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "Location not found with id: "
                        + request.getLocationId()
        ));

        Employee employee = new Employee();

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(department);
        employee.setLocation(location);
        employee.setDesignation(request.getDesignation());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setStatus(request.getStatus());

        Employee savedEmployee =
                employeeRepository.save(employee);

        return mapToResponse(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + id
                ));

        return mapToResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public EmployeeResponse updateEmployee(
            Long id,
            EmployeeRequest request
    ) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + id
                ));

        if (!employee.getEmployeeCode().equals(
                request.getEmployeeCode())
                && employeeRepository.existsByEmployeeCode(
                request.getEmployeeCode())) {

            throw new DuplicateResourceException(
                    "Employee code already exists: "
                            + request.getEmployeeCode()
            );
        }

        if (!employee.getEmail().equals(request.getEmail())
                && employeeRepository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "Employee email already exists: "
                            + request.getEmail()
            );
        }

        Department department = departmentRepository.findById(
                request.getDepartmentId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "Department not found with id: "
                        + request.getDepartmentId()
        ));

        Location location = locationRepository.findById(
                request.getLocationId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "Location not found with id: "
                        + request.getLocationId()
        ));

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(department);
        employee.setLocation(location);
        employee.setDesignation(request.getDesignation());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setStatus(request.getStatus());

        Employee updatedEmployee =
                employeeRepository.save(employee);

        return mapToResponse(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + id
                ));

        employeeRepository.delete(employee);
    }

    private EmployeeResponse mapToResponse(Employee employee) {

        EmployeeResponse response = new EmployeeResponse();

        response.setId(employee.getId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());

        if (employee.getDepartment() != null) {
            response.setDepartmentId(
                    employee.getDepartment().getId()
            );

            response.setDepartmentName(
                    employee.getDepartment().getName()
            );
        }

        if (employee.getLocation() != null) {
            response.setLocationId(
                    employee.getLocation().getId()
            );

            response.setLocationName(
                    employee.getLocation().getName()
            );
        }

        response.setDesignation(employee.getDesignation());
        response.setJoiningDate(employee.getJoiningDate());
        response.setStatus(employee.getStatus());
        response.setCreatedAt(employee.getCreatedAt());
        response.setUpdatedAt(employee.getUpdatedAt());

        return response;
    }
}