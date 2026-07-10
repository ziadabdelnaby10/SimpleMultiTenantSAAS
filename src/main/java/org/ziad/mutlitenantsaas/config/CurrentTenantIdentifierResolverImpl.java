package org.ziad.mutlitenantsaas.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.MultiTenancySettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Hibernate {@link CurrentTenantIdentifierResolver} that supplies the PostgreSQL schema
 * name for each Hibernate session.
 *
 * <p>Reads the current schema from {@link TenantContext#getCurrentSchema()} which is
 * populated by {@link org.ziad.mutlitenantsaas.security.filter.JwtAuthenticationFilter}
 * at the start of every authenticated request.
 * Falls back to {@code "public"} when no schema is set (e.g. for unauthenticated requests
 * or background jobs that operate on the public schema).
 *
 * <p>Also implements {@link HibernatePropertiesCustomizer} to register itself with
 * Hibernate without requiring additional XML or property configuration.
 */
@Component
@Slf4j
public class CurrentTenantIdentifierResolverImpl
        implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    /**
     * Returns the schema to use for the current Hibernate session.
     *
     * @return the current schema name, or {@code "public"} if none is set
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        final String schema = TenantContext.getCurrentSchema();
        log.debug("Resolving current tenant identifier for schema {}", schema);
        if (schema == null) {
            return "public";
        }
        return schema;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Returns {@code true} so that Hibernate validates that a tenant identifier
     * is present when an existing session is reused.
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    /**
     * Registers this resolver as the Hibernate multi-tenancy identifier resolver.
     *
     * @param hibernateProperties map of Hibernate configuration properties to customise
     */
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
