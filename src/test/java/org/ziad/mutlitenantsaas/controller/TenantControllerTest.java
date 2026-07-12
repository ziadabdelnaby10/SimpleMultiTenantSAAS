package org.ziad.mutlitenantsaas.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.ziad.mutlitenantsaas.aspect.GlobalExceptionHandler;
import org.ziad.mutlitenantsaas.dto.response.TenantResponse;
import org.ziad.mutlitenantsaas.entity.TenantStatus;
import org.ziad.mutlitenantsaas.service.TenantService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TenantControllerTest {

    private MockMvc mockMvc;

    private TenantService tenantService;

    @BeforeEach
    void setup() {
        this.tenantService = mock(TenantService.class);

        TenantController controller = new TenantController(this.tenantService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("PATCH /v1/tenants/approve/{tenantId} returns 200 when tenantId is valid")
    void approveTenantReturnsOkWhenTenantIdIsValid() throws Exception {
        doNothing().when(this.tenantService).approveTenant("tenant-1");

        this.mockMvc.perform(patch("/v1/tenants/approve/{tenantId}", "tenant-1"))
                .andExpect(status().isOk());

        verify(this.tenantService).approveTenant("tenant-1");
    }

    @Test
    @DisplayName("PATCH /v1/tenants/approve/{tenantId} returns 400 when tenantId is blank")
    void approveTenantReturnsBadRequestWhenTenantIdIsBlank() throws Exception {
        this.mockMvc.perform(patch("/v1/tenants/approve/{tenantId}", " "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.tenantId").exists());
    }

    @Test
    @DisplayName("GET /v1/tenants returns paginated tenants")
    void findAllTenantsReturnsPage() throws Exception {
        TenantResponse response = TenantResponse.builder()
                .tenantId("tenant-1")
                .companyName("Acme")
                .companyCode("acme")
                .email("contact@acme.com")
                .status(TenantStatus.ACTIVE)
                .build();
        when(this.tenantService.findAll(any()))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1));

        this.mockMvc.perform(get("/v1/tenants").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tenantId").value("tenant-1"))
                .andExpect(jsonPath("$.content[0].companyCode").value("acme"));
    }
}

