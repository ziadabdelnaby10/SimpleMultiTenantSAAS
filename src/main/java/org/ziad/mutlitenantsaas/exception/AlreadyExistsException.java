package org.ziad.mutlitenantsaas.exception;

/**
 * Thrown when an attempt is made to create a resource that already exists.
 *
 * <p>Examples: registering a tenant with a {@code companyCode} that is already taken,
 * or creating a user with a {@code username} / {@code email} that is already in use.
 *
 * <p>Handled by {@link org.ziad.mutlitenantsaas.aspect.GlobalExceptionHandler} which
 * maps it to {@code 409 Conflict}.
 */
public class AlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new exception with the supplied detail message.
     *
     * @param message human-readable description of the duplicate resource
     */
    public AlreadyExistsException(String message) {
        super(message);
    }
}
