package com.isera.assetmanagement.user.dto;

import java.util.Set;

public class MeResponse {

    private Long userId;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Boolean active;

    private Set<String> roles;

    private Long employeeId;

    private String employeeCode;

    private String designation;

    private String employeeStatus;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(
            Long userId
    ) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(
            String username
    ) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(
            String firstName
    ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(
            String lastName
    ) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(
            Boolean active
    ) {
        this.active = active;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(
            Set<String> roles
    ) {
        this.roles = roles;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(
            Long employeeId
    ) {
        this.employeeId = employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(
            String employeeCode
    ) {
        this.employeeCode = employeeCode;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(
            String designation
    ) {
        this.designation = designation;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(
            String employeeStatus
    ) {
        this.employeeStatus = employeeStatus;
    }
}