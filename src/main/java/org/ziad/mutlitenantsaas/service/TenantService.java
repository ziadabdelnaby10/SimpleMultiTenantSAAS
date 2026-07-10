package org.ziad.mutlitenantsaas.service;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ziad.mutlitenantsaas.dto.request.RegisterTenantRequest;
import org.ziad.mutlitenantsaas.dto.response.TenantResponse;
import org.ziad.mutlitenantsaas.entity.Tenant;

/**
 * Service contract for tenant lifecycle management.
 *
 * <p>Covers the full tenant lifecycle:
 * registration → approval (+ schema provisioning) → activate / deactivate / suspend.
 */
public interface TenantService {

    /**
     * Returns a managed {@link Tenant} reference by ID, or throws if not found.
     *
     * @param tenantId non-null, non-empty UUID of the tenant
     * @return the {@link Tenant} entity
     * @throws jakarta.persistence.EntityNotFoundException if no tenant with this ID exists
     */
    Tenant getReferenceById(@NotNull @NotEmpty String tenantId);

    /**
     * Registers a new tenant in {@link org.ziad.mutlitenantsaas.entity.TenantStatus#PENDING}
     * status. The tenant must subsequently be approved before its users can log in.
     *
     * @param request validated registration payload
     * @throws org.ziad.mutlitenantsaas.exception.AlreadyExistsException if the
     *         {@code companyCode} or {@code email} is already registered
     */
    void registerTenant(final RegisterTenantRequest request);

    /**
     * Approves a {@code PENDING} tenant: sets status to {@code ACTIVE}, provisions the
     * dedicated database schema, and fires a {@link org.ziad.mutlitenantsaas.event.TenantApprovedEvent}
     * so the initial admin user is created after the transaction commits.
     *
     * @param tenantId UUID of the tenant to approve
     * @throws jakarta.persistence.EntityNotFoundException if the tenant is not found
     * @throws IllegalStateException if the tenant is not in {@code PENDING} status
     */
    void approveTenant(final String tenantId);

    /**
     * Transitions a {@code SUSPENDED} or {@code INACTIVE} tenant back to {@code ACTIVE}.
     *
     * @param tenantId UUID of the tenant to activate
     * @throws jakarta.persistence.EntityNotFoundException if the tenant is not found
     * @throws IllegalStateException if the tenant is not in a re-activatable status
     */
    void activateTenant(final String tenantId);

    /**
     * Transitions an {@code ACTIVE} tenant to {@code INACTIVE}.
     *
     * @param tenantId UUID of the tenant to deactivate
     * @throws jakarta.persistence.EntityNotFoundException if the tenant is not found
     * @throws IllegalStateException if the tenant is not currently {@code ACTIVE}
     */
    void deactivateTenant(final String tenantId);

    /**
     * Transitions an {@code ACTIVE} tenant to {@code SUSPENDED}, blocking all tenant
     * user access immediately.
     *
     * @param tenantId UUID of the tenant to suspend
     * @throws jakarta.persistence.EntityNotFoundException if the tenant is not found
     * @throws IllegalStateException if the tenant is not currently {@code ACTIVE}
     */
    void suspendTenant(final String tenantId);

    /**
     * Returns a paginated list of all tenants on the platform.
     *
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of {@link TenantResponse} DTOs
     */
    Page<TenantResponse> findAll(Pageable pageable);
}
