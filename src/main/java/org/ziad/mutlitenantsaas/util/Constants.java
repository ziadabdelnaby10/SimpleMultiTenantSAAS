package org.ziad.mutlitenantsaas.util;

public final class Constants {
    public static final String TENANT_HEADER = "X-Tenant-ID";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String[] PUBLIC_URLS = {
            "/api/v1/auth/**",
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
