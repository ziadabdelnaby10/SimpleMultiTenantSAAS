package org.ziad.mutlitenantsaas.util;

/**
 * Application-wide string constants.
 *
 * <p>This is a non-instantiable constants class.
 */
public final class Constants {

    private Constants() {
        // prevent instantiation
    }

    /** Name of the HTTP request header that carries the tenant identifier. */
    public static final String TENANT_HEADER = "X-Tenant-ID";

    /** Name of the HTTP request header that carries the Bearer JWT token. */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * URL patterns that are accessible without authentication.
     * Includes Spring Security's standard authentication endpoints and all
     * Swagger / OpenAPI documentation paths.
     */
    public static final String[] PUBLIC_URLS = {
            "/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
}
