package org.ziad.mutlitenantsaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.ziad.mutlitenantsaas.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, String> {

    boolean existsByCompanyCode(String companyCode);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("update Tenant t set t.status = 'ACTIVE' where t.id = ?1")
    void activateTenant(String tenantId);

}