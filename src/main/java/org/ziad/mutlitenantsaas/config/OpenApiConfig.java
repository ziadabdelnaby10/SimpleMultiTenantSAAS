package org.ziad.mutlitenantsaas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@OpenAPIDefinition(
        info = @Info(
                title = "Multi-Tenant SaaS API",
                version = "1.0",
                description = "REST API for the Multi-Tenant SaaS platform. " +
                        "Tenant-scoped endpoints require the X-Tenant-ID request header.",
                contact = @Contact(
                        name = "Ziad Hassan",
                        email = "ziad@example.com"
                )
        ),
        servers = @Server(
                url = "http://localhost:8080/api",
                description = "Local Development"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "JWT token obtained from POST /v1/auth/login"
)
@Configuration
public class OpenApiConfig {

    /**
     * Injects the X-Tenant-ID header into all operations that belong to
     * tenant-scoped controllers (all except AuthenticationController and TenantController).
     */
    @Bean
    public OperationCustomizer tenantHeaderCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            String controllerName = handlerMethod.getBeanType().getSimpleName();
            boolean isTenantScoped = !controllerName.equals("AuthenticationController")
                    && !controllerName.equals("TenantController");
            if (isTenantScoped) {
                Parameter tenantHeader = new Parameter()
                        .in("header")
                        .name("X-Tenant-ID")
                        .description("Identifier of the tenant making the request")
                        .required(true)
                        .example("acme-corp");
                operation.addParametersItem(tenantHeader);
            }
            return operation;
        };
    }
}

