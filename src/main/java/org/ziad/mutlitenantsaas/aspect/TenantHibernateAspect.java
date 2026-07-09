package org.ziad.mutlitenantsaas.aspect;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.ziad.mutlitenantsaas.config.TenantContext;

//@Aspect
//@Component
public class TenantHibernateAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Before("execution(* org.ziad.mutlitenantsaas.service..*(..))")
    public void activateTenantFilter() {
        final String tenantId = TenantContext.getCurrentTenant();

        if (tenantId != null && !tenantId.isEmpty() && !tenantId.equalsIgnoreCase("all")) {
            entityManager.unwrap(Session.class)
                    .enableFilter("tenantFilter")
                    .setParameter("tenantId", tenantId);
        }

    }
}
