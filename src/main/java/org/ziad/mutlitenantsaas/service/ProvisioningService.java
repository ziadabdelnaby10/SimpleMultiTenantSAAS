package org.ziad.mutlitenantsaas.service;

import org.ziad.mutlitenantsaas.entity.Tenant;

/**
 * Service contract for provisioning a new tenant's database schema.
 *
 * <p>When a tenant is approved, this service is responsible for:
 * <ol>
 *   <li>Creating a dedicated PostgreSQL schema ({@code tenant_<companyCode>}).</li>
 *   <li>Running the tenant-specific Flyway migrations ({@code db/migration/tenant})
 *       to initialise tables such as {@code categories}, {@code products} and
 *       {@code stock_mvts}.</li>
 *   <li>Optionally seeding default data (reserved for future use).</li>
 * </ol>
 *
 * <p>If provisioning fails the implementation must roll back the partially created
 * schema before rethrowing a
 * {@link org.ziad.mutlitenantsaas.exception.TenantProvisionException}.
 */
public interface ProvisioningService {

    /**
     * Provisions a dedicated database schema for the given tenant.
     *
     * @param tenant the newly approved tenant whose schema must be created
     * @throws org.ziad.mutlitenantsaas.exception.TenantProvisionException if schema
     *         creation or migration fails
     */
    void provision(Tenant tenant);
}
