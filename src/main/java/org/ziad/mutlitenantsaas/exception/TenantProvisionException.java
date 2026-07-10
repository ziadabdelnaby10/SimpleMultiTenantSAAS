package org.ziad.mutlitenantsaas.exception;

/**
 * Thrown when the automated provisioning of a new tenant's database schema fails.
 *
 * <p>Raised by {@link org.ziad.mutlitenantsaas.service.impl.ProvisioningServiceImpl}
 * after it has attempted to roll back the partially created schema. The original cause
 * is logged before this exception is thrown.
 *
 * <p>Mapped to {@code 500 Internal Server Error} by the global exception handler.
 */
public class TenantProvisionException extends RuntimeException {

    /**
     * Constructs a new exception with the supplied detail message.
     *
     * @param message description of the provisioning failure
     */
    public TenantProvisionException(String message) {
        super(message);
    }
}
