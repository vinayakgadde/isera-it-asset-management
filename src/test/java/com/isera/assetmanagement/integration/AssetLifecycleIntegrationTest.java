package com.isera.assetmanagement.integration;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssetLifecycleIntegrationTest {

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
    // Existing test data
    // =========================================================

    private static final long ASSET_ID = 2L;
    private static final long EMPLOYEE_ID = 1L;

    // =========================================================
    // TEST 1
    // Login successfully
    // =========================================================

    @Test
    void shouldLoginSuccessfully() throws Exception {

        String loginRequest = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(
                ADMIN_USERNAME,
                ADMIN_PASSWORD
        );

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest)
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                containsString("accessToken")
                        )
                )
                .andExpect(
                        content().string(
                                containsString("refreshToken")
                        )
                );
    }

    // =========================================================
    // TEST 2
    // Complete Asset Lifecycle
    //
    // Login
    //   ↓
    // Verify asset IN_STOCK
    //   ↓
    // Assign asset
    //   ↓
    // Assignment status = ACTIVE
    //   ↓
    // Verify asset status = ASSIGNED
    //   ↓
    // Verify assignment
    //   ↓
    // Return asset
    //   ↓
    // Assignment status = RETURNED
    //   ↓
    // Verify asset status = IN_STOCK
    //   ↓
    // Verify audit logs
    // =========================================================

    @Test
    void shouldCompleteAssetAssignmentAndReturnLifecycle()
            throws Exception {

        // -----------------------------------------------------
        // STEP 1
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
                    "Access token was not returned from login API"
            );
        }

        String bearerToken =
                "Bearer " + accessToken;

        // -----------------------------------------------------
        // STEP 2
        // Verify asset is initially IN_STOCK
        // -----------------------------------------------------

        mockMvc.perform(
                        get("/api/v1/assets/{id}", ASSET_ID)
                                .header(
                                        "Authorization",
                                        bearerToken
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                containsString("IN_STOCK")
                        )
                );

        // -----------------------------------------------------
        // STEP 3
        // Create assignment
        // -----------------------------------------------------

        String assignmentRequest = """
                {
                    "assetId": %d,
                    "employeeId": %d,
                    "remarks": "Integration test asset assignment"
                }
                """.formatted(
                ASSET_ID,
                EMPLOYEE_ID
        );

        String assignmentResponse =
                mockMvc.perform(
                                post("/api/v1/asset-assignments")
                                        .header(
                                                "Authorization",
                                                bearerToken
                                        )
                                        .contentType(
                                                MediaType.APPLICATION_JSON
                                        )
                                        .content(
                                                assignmentRequest
                                        )
                        )
                        .andExpect(status().isCreated())
                        .andExpect(
                                content().string(
                                        containsString("ACTIVE")
                                )
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        // -----------------------------------------------------
        // STEP 4
        // Read assignment ID
        // -----------------------------------------------------

        JsonNode assignmentJson =
                jsonMapper.readTree(
                        assignmentResponse
                );

        long assignmentId =
                assignmentJson
                        .path("id")
                        .asLong();

        if (assignmentId <= 0) {

            throw new IllegalStateException(
                    "Assignment ID was not returned from assignment API"
            );
        }

        // -----------------------------------------------------
        // STEP 5
        // Verify asset status changed to ASSIGNED
        // -----------------------------------------------------

        mockMvc.perform(
                        get("/api/v1/assets/{id}", ASSET_ID)
                                .header(
                                        "Authorization",
                                        bearerToken
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                containsString("ASSIGNED")
                        )
                );

        // -----------------------------------------------------
        // STEP 6
        // Verify assignment record
        // -----------------------------------------------------

        mockMvc.perform(
                        get(
                                "/api/v1/asset-assignments/{id}",
                                assignmentId
                        )
                                .header(
                                        "Authorization",
                                        bearerToken
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                containsString(
                                        String.valueOf(ASSET_ID)
                                )
                        )
                )
                .andExpect(
                        content().string(
                                containsString(
                                        String.valueOf(EMPLOYEE_ID)
                                )
                        )
                )
                .andExpect(
                        content().string(
                                containsString("ACTIVE")
                        )
                );

        // -----------------------------------------------------
        // STEP 7
        // Return asset
        // -----------------------------------------------------

        mockMvc.perform(
                        post(
                                "/api/v1/asset-assignments/{id}/return",
                                assignmentId
                        )
                                .header(
                                        "Authorization",
                                        bearerToken
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                containsString("RETURNED")
                        )
                );

        // -----------------------------------------------------
        // STEP 8
        // Verify asset is back to IN_STOCK
        // -----------------------------------------------------

        mockMvc.perform(
                        get("/api/v1/assets/{id}", ASSET_ID)
                                .header(
                                        "Authorization",
                                        bearerToken
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                containsString("IN_STOCK")
                        )
                );

        // -----------------------------------------------------
        // STEP 9
        // Verify assignment is RETURNED
        // -----------------------------------------------------

        mockMvc.perform(
                        get(
                                "/api/v1/asset-assignments/{id}",
                                assignmentId
                        )
                                .header(
                                        "Authorization",
                                        bearerToken
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                containsString("RETURNED")
                        )
                );

        // -----------------------------------------------------
        // STEP 10
        // Verify audit logs
        // -----------------------------------------------------

        mockMvc.perform(
                        get("/api/v1/audit-logs")
                                .header(
                                        "Authorization",
                                        bearerToken
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                containsString("ASSET_ASSIGNED")
                        )
                )
                .andExpect(
                        content().string(
                                containsString("ASSET_RETURNED")
                        )
                );
    }
}