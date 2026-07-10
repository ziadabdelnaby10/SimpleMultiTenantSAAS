package org.ziad.mutlitenantsaas.exception;

/**
 * Thrown when a request reaches a tenant-scoped endpoint without the required
 * {@code X-Tenant-ID} HTTP header.
 *
 * <p>Handled by {@link org.ziad.mutlitenantsaas.aspect.GlobalExceptionHandler} which
 * maps it to {@code 400 Bad Request}.
 */
public class MissingTenantHeaderException extends RuntimeException {

    /**
     * Constructs a new exception with the default message
     * {@code "Tenant header not found"}.
     */
    public MissingTenantHeaderException() {
        super("Tenant header not found");
    }
}
