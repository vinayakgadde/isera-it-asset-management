package com.isera.assetmanagement.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LocationRequest {

    @NotBlank(message = "Location name is required")
    @Size(
            max = 100,
            message = "Location name must not exceed 100 characters"
    )
    private String name;

    @NotBlank(message = "Location code is required")
    @Size(
            max = 30,
            message = "Location code must not exceed 30 characters"
    )
    private String code;

    @NotBlank(message = "City is required")
    @Size(
            max = 100,
            message = "City must not exceed 100 characters"
    )
    private String city;

    @Size(
            max = 100,
            message = "State must not exceed 100 characters"
    )
    private String state;

    @NotBlank(message = "Country is required")
    @Size(
            max = 100,
            message = "Country must not exceed 100 characters"
    )
    private String country;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}