package org.ziad.mutlitenantsaas.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.MultiTenancySettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Hibernate {@link MultiTenantConnectionProvider} that implements schema-based
 * multi-tenancy by issuing a {@code SET search_path} command on every borrowed connection.
 *
 * <p>Strategy:
 * <ol>
 *   <li>Obtain a connection from the shared {@link DataSource} (HikariCP pool).</li>
 *   <li>Execute {@code SET search_path TO <schema>, public} so Hibernate resolves
 *       unqualified table names against the tenant schema first, then falls back to
 *       {@code public} for shared tables (e.g. {@code tenants}).</li>
 *   <li>On release, reset the search path to {@code public} before returning the
 *       connection to the pool, preventing schema leakage to the next borrower.</li>
 * </ol>
 *
 * <p>Also implements {@link HibernatePropertiesCustomizer} to self-register with
 * Hibernate at startup.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MultiTenantConnectionProviderImpl
        implements MultiTenantConnectionProvider<String>, HibernatePropertiesCustomizer {

    private final DataSource dataSource;

    /**
     * Returns any connection from the pool; used when no tenant context is required.
     *
     * @return an open JDBC connection
     * @throws SQLException if the pool cannot provide a connection
     */
    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Returns the connection back to the pool without resetting the search path.
     *
     * @param connection the connection to release
     * @throws SQLException if closing fails
     */
    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * Borrows a connection and sets the PostgreSQL {@code search_path} to the
     * provided tenant schema so all subsequent queries are routed to that schema.
     *
     * @param tenantIdentifier the schema name (e.g. {@code "tenant_acme-corp"})
     * @return a connection with the search path already set
     * @throws SQLException if the connection cannot be obtained or the search path
     *                      cannot be set
     */
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        log.debug("Getting connection for tenant: {}", tenantIdentifier);
        final Connection connection = getAnyConnection();
        try {
            if (tenantIdentifier != null && !tenantIdentifier.equals("public")) {
                connection.createStatement().execute("SET search_path TO " + tenantIdentifier + ", public");
                log.trace("Search path for tenant: {}", tenantIdentifier);
            }
        } catch (SQLException e) {
            log.error("Error setting schema for tenant: {}", tenantIdentifier, e);
            throw e;
        }
        return connection;
    }

    /**
     * Resets the PostgreSQL {@code search_path} back to {@code public} and closes
     * the connection, returning it to the pool safely.
     *
     * @param s          the tenant identifier (unused during release)
     * @param connection the connection to release
     * @throws SQLException if resetting the search path or closing the connection fails
     */
    @Override
    public void releaseConnection(String s, Connection connection) throws SQLException {
        try (connection) {
            connection.createStatement().execute("SET search_path TO public");
        } catch (SQLException e) {
            log.error("Error resetting schema to public", e);
            throw e;
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }

    /**
     * Registers this provider as the Hibernate multi-tenancy connection provider.
     *
     * @param hibernateProperties map of Hibernate configuration properties to customise
     */
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
    }
}
