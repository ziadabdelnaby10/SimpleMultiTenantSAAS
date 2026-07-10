package org.ziad.mutlitenantsaas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ziad.mutlitenantsaas.dto.response.TenantResponse;
import org.ziad.mutlitenantsaas.service.TenantService;

@Tag(name = "Tenant", description = "Tenant management API — platform-admin only, no X-Tenant-ID required")
@SecurityRequirement(name = "bearerAuth")
@Validated
@RestController
@RequestMapping("v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @Operation(summary = "Approve tenant", description = "Approve a pending tenant registration. Triggers database schema provisioning.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tenant approved successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid tenant ID", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tenant not found", content = @Content)
    })
    @PatchMapping("/approve/{tenantId}")
    public ResponseEntity<Void> approveTenant(
            @PathVariable("tenantId") @NotBlank(message = "Tenant ID must not be blank") final String tenantId
    ) {
        this.tenantService.approveTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Activate tenant", description = "Set a tenant's status to ACTIVE, restoring access for all tenant users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tenant activated", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tenant not found", content = @Content)
    })
    @PatchMapping("/activate/{tenantId}")
    public ResponseEntity<Void> activateTenant(
            @PathVariable("tenantId") @NotBlank(message = "Tenant ID must not be blank") final String tenantId
    ) {
        this.tenantService.activateTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deactivate tenant", description = "Set a tenant's status to INACTIVE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tenant deactivated", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tenant not found", content = @Content)
    })
    @PatchMapping("/deactivate/{tenantId}")
    public ResponseEntity<Void> deactivateTenant(
            @PathVariable("tenantId") @NotBlank(message = "Tenant ID must not be blank") final String tenantId
    ) {
        this.tenantService.deactivateTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Suspend tenant", description = "Set a tenant's status to SUSPENDED. Blocks all access for tenant users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tenant suspended", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tenant not found", content = @Content)
    })
    @PatchMapping("/suspend/{tenantId}")
    public ResponseEntity<Void> suspendTenant(
            @PathVariable("tenantId") @NotBlank(message = "Tenant ID must not be blank") final String tenantId
    ) {
        this.tenantService.suspendTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "List tenants", description = "Retrieve a paginated list of all tenants registered on the platform.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of tenants returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<TenantResponse>> findAllTenants(
            @ParameterObject
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.tenantService.findAll(pageable));
    }
}
