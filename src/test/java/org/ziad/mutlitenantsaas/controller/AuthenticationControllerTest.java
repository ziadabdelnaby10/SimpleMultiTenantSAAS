package org.ziad.mutlitenantsaas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.ziad.mutlitenantsaas.aspect.GlobalExceptionHandler;
import org.ziad.mutlitenantsaas.dto.request.LoginRequest;
import org.ziad.mutlitenantsaas.dto.request.RegisterTenantRequest;
import org.ziad.mutlitenantsaas.dto.response.LoginResponse;
import org.ziad.mutlitenantsaas.service.AuthenticationService;
import org.ziad.mutlitenantsaas.service.TenantService;
import org.ziad.mutlitenantsaas.support.TestRequestFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest {

    private MockMvc mockMvc;

    private AuthenticationService authenticationService;

    private TenantService tenantService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.authenticationService = mock(AuthenticationService.class);
        this.tenantService = mock(TenantService.class);
        this.objectMapper = new ObjectMapper();

        AuthenticationController controller = new AuthenticationController(this.authenticationService, this.tenantService);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /v1/auth/login returns 200 and token for valid credentials")
    void loginReturnsOkWhenPayloadIsValid() throws Exception {
        LoginRequest request = TestRequestFactory.validLoginRequest();
        when(this.authenticationService.login(any(LoginRequest.class)))
                .thenReturn(LoginResponse.builder().accessToken("jwt-token").tokenType("Bearer").build());

        this.mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    @DisplayName("POST /v1/auth/login returns 400 for invalid payload")
    void loginReturnsBadRequestWhenPayloadIsInvalid() throws Exception {
        LoginRequest request = LoginRequest.builder().username(" ").password("").build();

        this.mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.username").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    @DisplayName("POST /v1/auth/register returns 201 for valid payload")
    void registerReturnsCreatedWhenPayloadIsValid() throws Exception {
        RegisterTenantRequest request = TestRequestFactory.validRegisterTenantRequest();
        doNothing().when(this.tenantService).registerTenant(any(RegisterTenantRequest.class));

        this.mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(this.tenantService).registerTenant(any(RegisterTenantRequest.class));
    }

    @Test
    @DisplayName("POST /v1/auth/register returns 400 for invalid payload")
    void registerReturnsBadRequestWhenPayloadIsInvalid() throws Exception {
        RegisterTenantRequest request = RegisterTenantRequest.builder()
                .companyName("")
                .companyCode("invalid code")
                .email("invalid-email")
                .adminFullName("")
                .adminEmail("invalid-email")
                .adminUsername("ab")
                .adminPassword("123")
                .build();

        this.mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.companyName").exists())
                .andExpect(jsonPath("$.errors.companyCode").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }
}


