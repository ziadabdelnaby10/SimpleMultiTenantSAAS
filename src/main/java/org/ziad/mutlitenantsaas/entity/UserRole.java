package org.ziad.mutlitenantsaas.entity;

/**
 * Roles that govern access control within the Multi-Tenant SaaS platform.
 *
 * <p>Roles are stored as strings in the database ({@code @Enumerated(STRING)}) and
 * exposed as Spring Security {@link org.springframework.security.core.GrantedAuthority}
 * values via {@link User#getAuthorities()}.
 *
 * <p>Role hierarchy (highest → lowest privilege):
 * <ol>
 *   <li>{@link #ROLE_PLATFORM_ADMIN} — cross-tenant superuser</li>
 *   <li>{@link #ROLE_COMPANY_ADMIN}  — full control within a single tenant</li>
 *   <li>{@link #ROLE_ADMINISTRATOR}  — read-write within a single tenant</li>
 *   <li>{@link #ROLE_SALES_OPERATOR} — sales-specific operations</li>
 *   <li>{@link #ROLE_USER}           — read-only / basic access</li>
 * </ol>
 */
public enum UserRole {

    /** Platform-level superuser; can manage all tenants. Cannot be assigned via the API. */
    ROLE_PLATFORM_ADMIN,

    /** Company administrator; full CRUD over users, products and categories within their tenant. */
    ROLE_COMPANY_ADMIN,

    /** Tenant administrator; read-write access to inventory data within their tenant. */
    ROLE_ADMINISTRATOR,

    /** Standard user; basic read and limited write access. */
    ROLE_USER,

    /** Sales operator; allowed to record sales-related stock movements. */
    ROLE_SALES_OPERATOR
}