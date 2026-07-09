package org.ziad.mutlitenantsaas.service;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ziad.mutlitenantsaas.dto.request.RegisterTenantRequest;
import org.ziad.mutlitenantsaas.dto.response.TenantResponse;
import org.ziad.mutlitenantsaas.entity.Tenant;

public interface TenantService {

    Tenant getReferenceById(@NotNull @NotEmpty String tenantId);

    void registerTenant(final RegisterTenantRequest request);

    void approveTenant(final String tenantId);

    void activateTenant(final String tenantId);

    void deactivateTenant(final String tenantId);

    void suspendTenant(final String tenantId);

    Page<TenantResponse> findAll(Pageable pageable);
}
