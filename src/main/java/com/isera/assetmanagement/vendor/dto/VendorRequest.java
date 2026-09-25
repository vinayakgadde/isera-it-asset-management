package com.isera.assetmanagement.vendor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VendorRequest {

    @NotBlank(message = "Vendor name is required")
    @Size(max = 150, message = "Vendor name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Vendor code is required")
    @Size(max = 50, message = "Vendor code must not exceed 50 characters")
    private String code;

    @Size(max = 100, message = "Contact person must not exceed 100 characters")
    private String contactPerson;

    @Email(message = "Please provide a valid email address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    private Boolean active = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}