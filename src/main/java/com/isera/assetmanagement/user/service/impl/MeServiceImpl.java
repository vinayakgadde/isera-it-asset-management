package com.isera.assetmanagement.user.service.impl;

import com.isera.assetmanagement.assignment.entity.AssetAssignment;
import com.isera.assetmanagement.assignment.repository.AssetAssignmentRepository;
import com.isera.assetmanagement.employee.entity.Employee;
import com.isera.assetmanagement.employee.repository.EmployeeRepository;
import com.isera.assetmanagement.exception.ResourceNotFoundException;
import com.isera.assetmanagement.security.service.CurrentUserService;
import com.isera.assetmanagement.user.dto.MeResponse;
import com.isera.assetmanagement.user.dto.MyAssetResponse;
import com.isera.assetmanagement.user.dto.MyAssignmentResponse;
import com.isera.assetmanagement.user.entity.User;
import com.isera.assetmanagement.user.service.MeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MeServiceImpl implements MeService {

    private final CurrentUserService currentUserService;
    private final EmployeeRepository employeeRepository;
    private final AssetAssignmentRepository assignmentRepository;

    public MeServiceImpl(
            CurrentUserService currentUserService,
            EmployeeRepository employeeRepository,
            AssetAssignmentRepository assignmentRepository
    ) {
        this.currentUserService =
                currentUserService;

        this.employeeRepository =
                employeeRepository;

        this.assignmentRepository =
                assignmentRepository;
    }

    @Override
    public MeResponse getCurrentUser() {

        User user =
                currentUserService.getCurrentUser();

        MeResponse response =
                new MeResponse();

        response.setUserId(
                user.getId()
        );

        response.setUsername(
                user.getUsername()
        );

        response.setEmail(
                user.getEmail()
        );

        response.setFirstName(
                user.getFirstName()
        );

        response.setLastName(
                user.getLastName()
        );

        response.setActive(
                user.getActive()
        );

        response.setRoles(
                user.getRoles()
                        .stream()
                        .map(role ->
                                role.getName()
                        )
                        .collect(
                                Collectors.toSet()
                        )
        );

        Employee employee =
                employeeRepository
                        .findByUserId(
                                user.getId()
                        )
                        .orElse(null);

        if (employee != null) {

            response.setEmployeeId(
                    employee.getId()
            );

            response.setEmployeeCode(
                    employee.getEmployeeCode()
            );

            response.setDesignation(
                    employee.getDesignation()
            );

            response.setEmployeeStatus(
                    employee.getStatus()
            );
        }

        return response;
    }

    @Override
    public List<MyAssetResponse> getMyAssets() {

        User user =
                currentUserService.getCurrentUser();

        Employee employee =
                employeeRepository
                        .findByUserId(
                                user.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee profile is not linked "
                                                + "to the current user"
                                )
                        );

        return assignmentRepository
                .findByEmployeeIdAndStatus(
                        employee.getId(),
                        "ACTIVE"
                )
                .stream()
                .map(this::mapToMyAssetResponse)
                .toList();
    }

    @Override
    public List<MyAssignmentResponse>
    getMyAssignments() {

        User user =
                currentUserService.getCurrentUser();

        Employee employee =
                employeeRepository
                        .findByUserId(
                                user.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee profile is not linked "
                                                + "to the current user"
                                )
                        );

        return assignmentRepository
                .findByEmployeeId(
                        employee.getId()
                )
                .stream()
                .map(this::mapToMyAssignmentResponse)
                .toList();
    }

    private MyAssetResponse mapToMyAssetResponse(
            AssetAssignment assignment
    ) {

        MyAssetResponse response =
                new MyAssetResponse();

        response.setAssignmentId(
                assignment.getId()
        );

        if (assignment.getAsset() != null) {

            response.setAssetId(
                    assignment.getAsset().getId()
            );

            response.setAssetTag(
                    assignment.getAsset().getAssetTag()
            );

            response.setSerialNumber(
                    assignment.getAsset().getSerialNumber()
            );

            response.setBrand(
                    assignment.getAsset().getBrand()
            );

            response.setModel(
                    assignment.getAsset().getModel()
            );

            response.setPurchaseDate(
                    assignment.getAsset().getPurchaseDate()
            );

            response.setWarrantyExpiryDate(
                    assignment.getAsset()
                            .getWarrantyExpiryDate()
            );

            response.setAssetStatus(
                    assignment.getAsset().getStatus()
            );

            response.setAssetCondition(
                    assignment.getAsset().getCondition()
            );

            if (assignment.getAsset().getCategory()
                    != null) {

                response.setCategoryName(
                        assignment.getAsset()
                                .getCategory()
                                .getName()
                );
            }

            if (assignment.getAsset().getLocation()
                    != null) {

                response.setLocationName(
                        assignment.getAsset()
                                .getLocation()
                                .getName()
                );
            }
        }

        response.setAssignedDate(
                assignment.getAssignedDate()
        );

        response.setAssignmentStatus(
                assignment.getStatus()
        );

        response.setRemarks(
                assignment.getRemarks()
        );

        return response;
    }

    private MyAssignmentResponse mapToMyAssignmentResponse(
            AssetAssignment assignment
    ) {

        MyAssignmentResponse response =
                new MyAssignmentResponse();

        response.setAssignmentId(
                assignment.getId()
        );

        if (assignment.getAsset() != null) {

            response.setAssetId(
                    assignment.getAsset().getId()
            );

            response.setAssetTag(
                    assignment.getAsset().getAssetTag()
            );

            response.setSerialNumber(
                    assignment.getAsset().getSerialNumber()
            );

            response.setBrand(
                    assignment.getAsset().getBrand()
            );

            response.setModel(
                    assignment.getAsset().getModel()
            );

            if (assignment.getAsset().getCategory()
                    != null) {

                response.setCategoryName(
                        assignment.getAsset()
                                .getCategory()
                                .getName()
                );
            }

            if (assignment.getAsset().getLocation()
                    != null) {

                response.setLocationName(
                        assignment.getAsset()
                                .getLocation()
                                .getName()
                );
            }
        }

        response.setAssignedDate(
                assignment.getAssignedDate()
        );

        response.setReturnedDate(
                assignment.getReturnedDate()
        );

        response.setStatus(
                assignment.getStatus()
        );

        response.setRemarks(
                assignment.getRemarks()
        );

        return response;
    }
}