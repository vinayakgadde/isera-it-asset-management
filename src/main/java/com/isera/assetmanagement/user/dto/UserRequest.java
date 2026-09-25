package com.isera.assetmanagement.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UserRequest {

    @NotBlank(message = "Username is required")
    @Size(
            max = 50,
            message = "Username must not exceed 50 characters"
    )
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(
            max = 150,
            message = "Email must not exceed 150 characters"
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Size(
            min = 8,
            max = 100,
            message = "Password must be between 8 and 100 characters"
    )
    private String password;

    @NotBlank(message = "First name is required")
    @Size(
            max = 100,
            message = "First name must not exceed 100 characters"
    )
    private String firstName;

    @Size(
            max = 100,
            message = "Last name must not exceed 100 characters"
    )
    private String lastName;

    private Boolean active = true;

    @NotNull(message = "Role IDs are required")
    @Size(
            min = 1,
            message = "At least one role is required"
    )
    private Set<Long> roleIds;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }
}