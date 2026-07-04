package org.ziad.mutlitenantsaas.security.filter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.ziad.mutlitenantsaas.config.TenantContext;

@Aspect
@Component
public class TenantHibernateFilter {

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
