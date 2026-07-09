package org.ziad.mutlitenantsaas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ziad.mutlitenantsaas.dto.response.TenantResponse;
import org.ziad.mutlitenantsaas.service.TenantService;

@RestController
@RequestMapping("v1/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant", description = "Tenant API")
public class TenantController {

    private final TenantService service;

    @PostMapping("/approve/{tenant-id}")
    public ResponseEntity<Void> approveTenant(
            @PathVariable("tenant-id") final String tenantId
    ) {
        this.service.approveTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate/{tenant-id}")
    public ResponseEntity<Void> activateTenant(
            @PathVariable("tenant-id") final String tenantId
    ) {
        this.service.activateTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate/{tenant-id}")
    public ResponseEntity<Void> deactivateTenant(
            @PathVariable("tenant-id") final String tenantId
    ) {
        this.service.deactivateTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/suspend/{tenant-id}")
    public ResponseEntity<Void> suspendTenant(
            @PathVariable("tenant-id") final String tenantId
    ) {
        this.service.suspendTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<TenantResponse>> findAllTenants(
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.service.findAll(pageable));
    }
}
