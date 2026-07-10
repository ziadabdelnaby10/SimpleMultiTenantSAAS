package org.ziad.mutlitenantsaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.ziad.mutlitenantsaas.entity.Tenant;

/**
 * Spring Data JPA repository for {@link org.ziad.mutlitenantsaas.entity.Tenant} entities.
 * Stored in the {@code public} schema and shared across all requests.
 */
public interface TenantRepository extends JpaRepository<Tenant, String> {

    /**
     * Checks whether a tenant with the given company code already exists.
     *
     * @param companyCode the unique company code / slug to check
     * @return {@code true} if a tenant with this code exists
     */
    boolean existsByCompanyCode(String companyCode);

    /**
     * Checks whether a tenant with the given email already exists.
     *
     * @param email the primary company contact email to check
     * @return {@code true} if a tenant with this email exists
     */
    boolean existsByEmail(String email);

    /**
     * Directly activates a tenant by setting its status to {@code ACTIVE}.
     * Prefer the service-layer method which validates the current status first.
     *
     * @param tenantId UUID of the tenant to activate
     */
    @Transactional
    @Modifying
    @Query("update Tenant t set t.status = 'ACTIVE' where t.id = ?1")
    void activateTenant(String tenantId);

}