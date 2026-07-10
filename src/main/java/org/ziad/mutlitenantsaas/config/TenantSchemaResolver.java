package org.ziad.mutlitenantsaas.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Resolves the PostgreSQL schema name for a given tenant ID.
 *
 * <p>Looks up the {@code company_code} column of the {@code public.tenants} table
 * and converts it to the schema name pattern {@code tenant_<companyCode>} (lower-cased).
 * Falls back to the {@code public} schema if the tenant is not found or an error occurs.
 *
 * <p>Results are cached under the {@code "tenantSchemas"} cache (configured in
 * {@link CacheConfig}) keyed by {@code tenantId}, so repeated calls within the same
 * JVM process do not hit the database.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantSchemaResolver {

    private final JdbcTemplate jdbcTemplate;

    private static final String PUBLIC_SCHEMA = "public";

    /**
     * Returns the PostgreSQL schema name for the given tenant, caching the result.
     *
     * <p>Returns {@code "public"} in the following cases:
     * <ul>
     *   <li>{@code tenantId} is {@code null}</li>
     *   <li>no active tenant row is found for {@code tenantId}</li>
     *   <li>a database or runtime error occurs</li>
     * </ul>
     *
     * @param tenantId the UUID of the tenant whose schema should be resolved
     * @return the schema name (e.g. {@code "tenant_acme-corp"}) or {@code "public"}
     */
    @Cacheable(value = "tenantSchemas", key = "#tenantId")
    public String resolveTenantSchema(final String tenantId) {
        if (tenantId == null) {
            return PUBLIC_SCHEMA;
        }

        try {
            final String companyCode = this.jdbcTemplate.queryForObject(
                    "SELECT company_code FROM public.tenants WHERE id = ? AND deleted = false",
                    String.class,
                    tenantId);
            if (companyCode != null) {
                final String schemaName = "tenant_" + companyCode.toLowerCase();
                log.debug("Resolved Tenant schema: {} for tenant: {}", schemaName, tenantId);
                return schemaName;
            }
            log.warn("Tenant schema not found for tenant: {}, using public schema", tenantId);
            return PUBLIC_SCHEMA;
        } catch (final Exception e) {
            log.error("Error resolving tenant schema for tenant: {}", tenantId, e);
            return PUBLIC_SCHEMA;
        }
    }
}