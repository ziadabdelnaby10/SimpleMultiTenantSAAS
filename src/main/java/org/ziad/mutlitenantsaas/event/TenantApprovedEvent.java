package org.ziad.mutlitenantsaas.event;

import org.ziad.mutlitenantsaas.entity.Tenant;

/**
 * Published when a tenant has been approved and provisioned.
 * Decouples tenant approval from admin-user creation to avoid a
 * circular dependency between TenantService and UserService.
 */
public record TenantApprovedEvent(Tenant tenant) {
}

