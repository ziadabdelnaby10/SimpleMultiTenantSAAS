package org.ziad.mutlitenantsaas.exception;

/**
 * Thrown when a business-rule authorization check fails at the service layer.
 *
 * <p>Distinct from Spring Security's access-denied mechanism: this exception is used
 * for application-level rules such as preventing a user from assigning the
 * {@code ROLE_PLATFORM_ADMIN} role or attempting an action on a resource that belongs
 * to a different tenant.
 *
 * <p>Mapped to {@code 403 Forbidden} by the global exception handler.
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructs a new exception with the supplied detail message.
     *
     * @param message description of the authorization failure
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
