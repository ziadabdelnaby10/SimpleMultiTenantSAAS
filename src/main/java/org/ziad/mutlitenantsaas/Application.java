package org.ziad.mutlitenantsaas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Multi-Tenant SaaS Application entry point.
 *
 * <p>Bootstraps the Spring Boot application with:
 * <ul>
 *   <li>{@code @EnableJpaAuditing} — automatically populates {@code createdAt},
 *       {@code updatedAt}, {@code createdBy} and {@code updatedBy} fields on every
 *       {@link org.ziad.mutlitenantsaas.common.AbstractEntity} via Spring Data auditing.</li>
 *   <li>{@code @EnableSpringDataWebSupport} with {@code VIA_DTO} serialization mode —
 *       serializes {@link org.springframework.data.domain.Page} responses as a stable
 *       JSON DTO instead of the internal Spring Data representation, ensuring a
 *       consistent and forward-compatible paginated response contract.</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class Application {

    /**
     * Application entry point.
     *
     * @param args command-line arguments passed to the JVM
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
