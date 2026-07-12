package org.ziad.mutlitenantsaas.integration;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Testcontainers
class CommonSchemaMigrationIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("saas_it")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    static void runFlywayMigration() {
        assumeTrue(DockerClientFactory.instance().isDockerAvailable(), "Docker is required for Testcontainers tests");
        Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .schemas("public")
                .locations("classpath:db/migration/common")
                .load()
                .migrate();
    }

    @Test
    @DisplayName("Common schema migration creates core tables")
    void migrateCommonSchemaCreatesTenantAndUserTables() throws Exception {
        try (Connection connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            assertTrue(tableExists(connection, "tenants"));
            assertTrue(tableExists(connection, "users"));
        }
    }

    @Test
    @DisplayName("Common schema migration seeds one platform admin user")
    void migrateCommonSchemaSeedsPlatformAdmin() throws Exception {
        try (Connection connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             PreparedStatement statement = connection.prepareStatement("select count(*) from users where username = ?")) {
            statement.setString(1, "john.admin");
            try (ResultSet resultSet = statement.executeQuery()) {
                assertTrue(resultSet.next());
                assertEquals(1, resultSet.getInt(1));
            }
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws Exception {
        try (PreparedStatement statement = connection.prepareStatement(
                "select exists (select 1 from information_schema.tables where table_schema = 'public' and table_name = ?)")) {
            statement.setString(1, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getBoolean(1);
            }
        }
    }
}


