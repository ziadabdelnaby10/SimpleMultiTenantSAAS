package org.ziad.mutlitenantsaas.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.ziad.mutlitenantsaas.service.UserService;

/**
 * Handles side effects of a tenant being approved.
 * Owns admin-user creation so that TenantService no longer depends on UserService.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantApprovedListener {

    private final UserService userService;

    /**
     * Runs after the approveTenant transaction commits, so the tenant row/status is
     * durably persisted before we create the admin user that references it.
     * Admin-user creation runs in its own new transaction.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTenantApproved(final TenantApprovedEvent event) {
        log.info("Creating admin user for approved tenant: {}", event.tenant().getId());
        userService.createRoleAdminUser(event.tenant());
    }
}

