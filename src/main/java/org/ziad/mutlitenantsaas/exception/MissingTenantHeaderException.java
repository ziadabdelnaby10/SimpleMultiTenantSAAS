package org.ziad.mutlitenantsaas.exception;

public class MissingTenantHeaderException extends RuntimeException {

    public MissingTenantHeaderException() {
        super("Tenant header not found");
    }
}
