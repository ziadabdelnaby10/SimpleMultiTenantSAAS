package org.ziad.mutlitenantsaas.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.ziad.mutlitenantsaas.entity.Tenant;
import org.ziad.mutlitenantsaas.exception.TenantProvisionException;
import org.ziad.mutlitenantsaas.service.ProvisioningService;

import javax.sql.DataSource;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public void provision(Tenant tenant) {
        final String schemaName = "tenant_" + tenant.getCompanyCode().toLowerCase();
        final String createSchemaSql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        jdbcTemplate.execute(createSchemaSql);

        try {
            log.info("Provisioning tenant: {}  (schema: {})", tenant.getCompanyName(), schemaName);

            createSchema(schemaName);

            runTenantMigration(schemaName);

            initializeDefaultData(schemaName, tenant);
        } catch (Exception e) {
            log.error("Error provisioning Tenant: {} , {}", tenant.getCompanyName(), e.getMessage());
            try {
                dropSchema(schemaName);
            }catch (Exception e1) {
                log.error("Error dropping Schema : {}", e1.getMessage());
            }
            throw new TenantProvisionException("Failed to provision tenant");
        }
    }

    private void dropSchema(String schemaName) {
        final String dropSchemaSql = String.format("DROP SCHEMA IF EXISTS %s", schemaName);
        jdbcTemplate.execute(dropSchemaSql);
    }

    private void initializeDefaultData(String schemaName, Tenant tenant) {

    }

    private void runTenantMigration(String schemaName) {
        log.info("Running tenant migration for tenant: {}", schemaName);
        final Flyway tenantFlyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schemaName)
                .locations("classpath:db/migration/tenant")
                .baselineOnMigrate(true)
                .table("flyway_schema_history")
                .validateOnMigrate(true)
                .cleanDisabled(true)
                .load();
        log.info("Tenant migration started.");
        tenantFlyway.migrate();
        log.info("Tenant migration completed.");
    }

    private void createSchema(final String schemaName) {
        final String createSchemaSql = String.format("CREATE SCHEMA IF NOT EXISTS %s", schemaName);
        jdbcTemplate.execute(createSchemaSql);
    }
}
