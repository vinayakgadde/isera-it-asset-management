package com.isera.assetmanagement.maintenance.controller;

import com.isera.assetmanagement.exception.GlobalExceptionHandler;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordRequest;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordResponse;
import com.isera.assetmanagement.maintenance.dto.MaintenanceRecordUpdateRequest;
import com.isera.assetmanagement.maintenance.service.MaintenanceRecordService;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MaintenanceRecordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MaintenanceRecordService maintenanceRecordService;

    @InjectMocks
    private MaintenanceRecordController maintenanceRecordController;

    @BeforeEach
    void setUp() {

        LocalValidatorFactoryBean validator =
                new LocalValidatorFactoryBean();

        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(maintenanceRecordController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldCreateMaintenance() throws Exception {

        MaintenanceRecordResponse response =
                new MaintenanceRecordResponse();

        when(maintenanceRecordService.createMaintenance(
                any(MaintenanceRecordRequest.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "assetId": 2,
                    "reportedByEmployeeId": 1,
                    "assignedTechnicianId": 1,
                    "issueDescription": "Laptop is overheating"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/maintenance")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated());

        verify(maintenanceRecordService)
                .createMaintenance(any(MaintenanceRecordRequest.class));
    }

    @Test
    void shouldGetAllMaintenance() throws Exception {

        MaintenanceRecordResponse response =
                new MaintenanceRecordResponse();

        when(maintenanceRecordService.getAllMaintenance())
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/v1/maintenance")
                )
                .andExpect(status().isOk());

        verify(maintenanceRecordService)
                .getAllMaintenance();
    }

    @Test
    void shouldGetMaintenanceById() throws Exception {

        MaintenanceRecordResponse response =
                new MaintenanceRecordResponse();

        when(maintenanceRecordService.getMaintenanceById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/maintenance/1")
                )
                .andExpect(status().isOk());

        verify(maintenanceRecordService)
                .getMaintenanceById(1L);
    }

    @Test
    void shouldUpdateMaintenance() throws Exception {

        MaintenanceRecordResponse response =
                new MaintenanceRecordResponse();

        when(maintenanceRecordService.updateMaintenance(
                eq(1L),
                any(MaintenanceRecordUpdateRequest.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "resolution": "Replaced cooling fan",
                    "cost": 2000.00
                }
                """;

        mockMvc.perform(
                        put("/api/v1/maintenance/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(maintenanceRecordService)
                .updateMaintenance(
                        eq(1L),
                        any(MaintenanceRecordUpdateRequest.class)
                );
    }

    @Test
    void shouldStartMaintenance() throws Exception {

        MaintenanceRecordResponse response =
                new MaintenanceRecordResponse();

        when(maintenanceRecordService.startMaintenance(1L))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/maintenance/1/start")
                )
                .andExpect(status().isOk());

        verify(maintenanceRecordService)
                .startMaintenance(1L);
    }

    @Test
    void shouldResolveMaintenance() throws Exception {

        MaintenanceRecordResponse response =
                new MaintenanceRecordResponse();

        when(maintenanceRecordService.resolveMaintenance(
                eq(1L),
                any(MaintenanceRecordUpdateRequest.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "resolution": "Cleaned cooling fan",
                    "cost": 1500.00
                }
                """;

        mockMvc.perform(
                        post("/api/v1/maintenance/1/resolve")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

        verify(maintenanceRecordService)
                .resolveMaintenance(
                        eq(1L),
                        any(MaintenanceRecordUpdateRequest.class)
                );
    }

    @Test
    void shouldCloseMaintenance() throws Exception {

        MaintenanceRecordResponse response =
                new MaintenanceRecordResponse();

        when(maintenanceRecordService.closeMaintenance(1L))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/maintenance/1/close")
                )
                .andExpect(status().isOk());

        verify(maintenanceRecordService)
                .closeMaintenance(1L);
    }

    @Test
    void shouldRejectInvalidCreateRequest() throws Exception {

        String requestJson = """
                {
                    "assetId": null,
                    "issueDescription": ""
                }
                """;

        mockMvc.perform(
                        post("/api/v1/maintenance")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isBadRequest());

        verify(maintenanceRecordService, never())
                .createMaintenance(any(MaintenanceRecordRequest.class));
    }
}