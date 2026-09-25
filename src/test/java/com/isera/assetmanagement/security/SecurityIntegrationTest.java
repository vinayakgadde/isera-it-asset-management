package com.isera.assetmanagement.security;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final JsonMapper jsonMapper =
            JsonMapper.builder().build();

    // =========================================================
    // Test credentials
    // =========================================================

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Admin@123";

    // =========================================================
    // TEST 1
    // Protected API without token
    // Expected: 401 Unauthorized
    // =========================================================

    @Test
    void shouldRejectRequestWithoutToken() throws Exception {

        mockMvc.perform(
                        get("/api/v1/dashboard/summary")
                )
                .andExpect(status().isUnauthorized());
    }

    // =========================================================
    // TEST 2
    // Valid admin login + protected API
    // Expected: 200 OK
    // =========================================================

    @Test
    void shouldAllowAdminWithValidToken() throws Exception {

        // -----------------------------------------------------
        // Login
        // -----------------------------------------------------

        String loginRequest = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(
                ADMIN_USERNAME,
                ADMIN_PASSWORD
        );

        String loginResponse =
                mockMvc.perform(
                                post("/api/v1/auth/login")
                                        .contentType(
                                                MediaType.APPLICATION_JSON
                                        )
                                        .content(loginRequest)
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode loginJson =
                jsonMapper.readTree(loginResponse);

        String accessToken =
                loginJson
                        .path("accessToken")
                        .asText();

        if (accessToken == null ||
                accessToken.isBlank()) {

            throw new IllegalStateException(
                    "Access token was not returned"
            );
        }

        // -----------------------------------------------------
        // Access protected endpoint
        // -----------------------------------------------------

        mockMvc.perform(
                        get("/api/v1/dashboard/summary")
                                .header(
                                        "Authorization",
                                        "Bearer " + accessToken
                                )
                )
                .andExpect(status().isOk());
    }

    // =========================================================
    // TEST 3
    // Invalid JWT
    // Expected: 401 Unauthorized
    // =========================================================

    @Test
    void shouldRejectInvalidToken() throws Exception {

        String invalidToken =
                "this-is-not-a-valid-jwt-token";

        mockMvc.perform(
                        get("/api/v1/assets/2")
                                .header(
                                        "Authorization",
                                        "Bearer " + invalidToken
                                )
                )
                .andExpect(status().isUnauthorized());
    }

    // =========================================================
    // TEST 4
    // Employee role accessing admin/management endpoint
    // Expected: 403 Forbidden
    //
    // @WithMockUser is used here to test the authorization rule
    // independently from the JWT login flow.
    // =========================================================

    @Test
    @WithMockUser(
            username = "employee1",
            roles = {"EMPLOYEE"}
    )
    void shouldRejectEmployeeFromDashboard() throws Exception {

        mockMvc.perform(
                        get("/api/v1/dashboard/summary")
                )
                .andExpect(status().isForbidden());
    }

    // =========================================================
    // TEST 5
    // Employee role accessing admin-only users API
    // Expected: 403 Forbidden
    // =========================================================

    @Test
    @WithMockUser(
            username = "employee1",
            roles = {"EMPLOYEE"}
    )
    void shouldRejectEmployeeFromUsersApi() throws Exception {

        mockMvc.perform(
                        get("/api/v1/users")
                )
                .andExpect(status().isForbidden());
    }
}