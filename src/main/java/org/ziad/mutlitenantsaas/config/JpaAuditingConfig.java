package org.ziad.mutlitenantsaas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

/** * Spring configuration that supplies the current auditor for JPA auditing. * * <p>When JPA auditing is enabled (via {@code @EnableJpaAuditing}), Spring Data * uses the {@link AuditorAware} bean defined here to populate auditing fields * such as {@code createdBy} and {@code lastModifiedBy} on entities (e.g. * {@code AbstractEntity}) whenever they are persisted or updated.</p> * * @see AuditorAware * @see org.springframework.data.jpa.repository.config.EnableJpaAuditing */
@Configuration
public class JpaAuditingConfig {

    /**     * Registers the {@link AuditorAware} bean used by Spring Data JPA auditing     * to resolve the identity of the currently authenticated user.     *     * @return an {@link AuditorAwareImpl} instance backed by the Spring Security context     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**     * {@link AuditorAware} implementation that derives the current auditor from     * the Spring Security {@link SecurityContextHolder}.     *     * <p>The auditor is resolved from the authentication principal. An empty     * result is returned when there is no authenticated user, so that auditing     * fields are left unset rather than attributed to an anonymous caller.</p>     */
    public static class AuditorAwareImpl implements AuditorAware<String> {

        /**         * Resolves the identity of the current auditor for the active request.         *         * <p>Returns {@link Optional#empty()} when any of the following hold:</p>         * <ul>         *     <li>there is no {@link Authentication} in the security context;</li>         *     <li>the authentication is not authenticated;</li>         *     <li>the principal is the anonymous user ({@code "anonymousUser"}).</li>         * </ul>         * Otherwise, the principal's string representation is returned as the auditor.         *         * @return an {@link Optional} containing the current auditor identifier,         *         or {@link Optional#empty()} if no authenticated user is present         */
        @Override
        public Optional<String> getCurrentAuditor() {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || Objects.equals(authentication.getPrincipal(), "anonymousUser")) {
                return Optional.empty();
            }
            if (authentication.getPrincipal() != null) {
                return Optional.of(authentication.getPrincipal().toString());
            }
            return Optional.empty();
        }
    }
}