package org.ziad.mutlitenantsaas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziad.mutlitenantsaas.dto.request.LoginRequest;
import org.ziad.mutlitenantsaas.dto.request.RegisterTenantRequest;
import org.ziad.mutlitenantsaas.dto.response.LoginResponse;
import org.ziad.mutlitenantsaas.service.AuthenticationService;
import org.ziad.mutlitenantsaas.service.TenantService;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final TenantService tenantService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody final LoginRequest request
    ) {
        final LoginResponse response = this.authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid
            @RequestBody final RegisterTenantRequest request
    ) {
        this.tenantService.registerTenant(request);
        return ResponseEntity.ok().build();
    }
}
