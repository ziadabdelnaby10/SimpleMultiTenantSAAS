package org.ziad.mutlitenantsaas.mapper;

import org.springframework.stereotype.Component;
import org.ziad.mutlitenantsaas.dto.request.RegisterTenantRequest;
import org.ziad.mutlitenantsaas.dto.response.TenantResponse;
import org.ziad.mutlitenantsaas.entity.Tenant;

import java.time.Instant;

/**
 * Converts between {@link Tenant} entity and its associated DTOs.
 *
 * <p>Note: {@code adminPassword} is intentionally excluded from
 * {@link #toResponse(Tenant)} to prevent sensitive data from being returned in API responses.
 */
@Component
public class TenantMapper {

    /**
     * Maps a {@link RegisterTenantRequest} to a new {@link Tenant} entity.
     * The returned entity has {@code status = null} and {@code adminPassword = null};
     * the caller is responsible for setting both before persisting.
     *
     * @param request the validated registration payload
     * @return a new, unpersisted {@link Tenant} entity
     */
    public Tenant toEntity(final RegisterTenantRequest request) {
        return Tenant.builder()
                .companyName(request.getCompanyName())
                .companyCode(request.getCompanyCode())
                .createdAt(Instant.now())
                .email(request.getEmail())
                .adminFullName(request.getAdminFullName())
                .adminEmail(request.getAdminEmail())
                .adminUsername(request.getAdminUsername())
                .deleted(false)
                .build();
    }

    /**
     * Maps a persisted {@link Tenant} entity to a {@link TenantResponse} DTO.
     * The {@code adminPassword} field is deliberately omitted.
     *
     * @param tenant the entity to convert
     * @return the corresponding response DTO
     */
    public TenantResponse toResponse(final Tenant tenant) {
        return TenantResponse.builder()
                .tenantId(tenant.getId())
                .companyName(tenant.getCompanyName())
                .companyCode(tenant.getCompanyCode())
                .createdAt(tenant.getCreatedAt())
                .email(tenant.getEmail())
                .adminFullName(tenant.getAdminFullName())
                .adminEmail(tenant.getAdminEmail())
                .adminUsername(tenant.getAdminUsername())
                .status(tenant.getStatus())
                .build();
    }
}
