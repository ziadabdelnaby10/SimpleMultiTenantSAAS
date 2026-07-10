package org.ziad.mutlitenantsaas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ziad.mutlitenantsaas.dto.request.LoginRequest;
import org.ziad.mutlitenantsaas.dto.request.RegisterTenantRequest;
import org.ziad.mutlitenantsaas.dto.response.LoginResponse;
import org.ziad.mutlitenantsaas.service.AuthenticationService;
import org.ziad.mutlitenantsaas.service.TenantService;

@Tag(name = "Authentication", description = "Public authentication endpoints — no JWT required")
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TenantService tenantService;

    @Operation(
            summary = "Login",
            description = "Authenticate with username and password. Returns a JWT access token to use in subsequent requests."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failure — missing or blank fields", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody final LoginRequest request
    ) {
        final LoginResponse response = this.authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Register Tenant",
            description = "Register a new company/tenant on the SaaS platform. Creates the tenant record and an initial admin account. Tenant must be approved by a platform admin before it becomes active."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tenant registered successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failure", content = @Content),
            @ApiResponse(responseCode = "409", description = "Company code or email already exists", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody final RegisterTenantRequest request
    ) {
        this.tenantService.registerTenant(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
