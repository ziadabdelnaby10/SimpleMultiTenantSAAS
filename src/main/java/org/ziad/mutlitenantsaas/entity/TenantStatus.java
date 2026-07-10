package org.ziad.mutlitenantsaas.entity;

/**
 * Lifecycle status of a {@link Tenant}.
 *
 * <p>Allowed transitions:
 * <pre>
 *   PENDING → ACTIVE      (via approve)
 *   ACTIVE  → SUSPENDED   (via suspend)
 *   ACTIVE  → INACTIVE    (via deactivate)
 *   SUSPENDED / INACTIVE → ACTIVE (via activate)
 * </pre>
 */
public enum TenantStatus {
    /** Newly registered; awaiting platform-admin approval and schema provisioning. */
    PENDING,
    /** Fully operational; users can log in and access tenant data. */
    ACTIVE,
    /** Temporarily blocked; all tenant users are denied access. */
    SUSPENDED,
    /** Administratively deactivated; access is revoked until re-activated. */
    INACTIVE
}