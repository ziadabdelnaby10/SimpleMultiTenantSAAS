package org.ziad.mutlitenantsaas.config;

/**
 * Thread-local holder that carries the current tenant context for the duration
 * of a single HTTP request.
 *
 * <p>Both the tenant ID (as registered in the {@code tenants} table) and the resolved
 * PostgreSQL schema name are stored separately:
 * <ul>
 *   <li>{@code currentTenant} — the UUID of the authenticated tenant, extracted from
 *       the JWT claim {@code tenant_id} by
 *       {@link org.ziad.mutlitenantsaas.security.filter.JwtAuthenticationFilter}.</li>
 *   <li>{@code currentSchema} — the PostgreSQL schema name (e.g. {@code tenant_acme-corp})
 *       resolved from the tenant ID and used by
 *       {@link CurrentTenantIdentifierResolverImpl} to route Hibernate queries.</li>
 * </ul>
 *
 * <p><strong>Usage contract:</strong> values must be {@linkplain #clear() cleared} at the
 * end of every request (typically in a {@code finally} block in the filter) to prevent
 * leakage between requests that reuse the same thread from the pool.
 */
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_SCHEMA = new ThreadLocal<>();

    /**
     * Sets the current tenant ID for this thread.
     *
     * @param tenant UUID of the authenticated tenant
     */
    public static void setCurrentTenant(final String tenant) {
        CURRENT_TENANT.set(tenant);
    }

    /**
     * Sets the resolved PostgreSQL schema name for this thread.
     *
     * @param schema schema name, e.g. {@code tenant_acme-corp}
     */
    public static void setCurrentSchema(final String schema) {
        CURRENT_SCHEMA.set(schema);
    }

    /**
     * Returns the current tenant ID, or {@code null} if none is set.
     *
     * @return tenant UUID string or {@code null}
     */
    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Returns the current PostgreSQL schema name, or {@code null} if none is set.
     *
     * @return schema name or {@code null}
     */
    public static String getCurrentSchema() {
        return CURRENT_SCHEMA.get();
    }

    /**
     * Removes both the tenant ID and the schema name from the current thread's
     * storage. <strong>Must</strong> be called at the end of every request.
     */
    public static void clear() {
        CURRENT_TENANT.remove();
        CURRENT_SCHEMA.remove();
    }
}
