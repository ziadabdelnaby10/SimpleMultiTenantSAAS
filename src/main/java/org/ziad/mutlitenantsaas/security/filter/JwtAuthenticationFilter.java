package org.ziad.mutlitenantsaas.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.ziad.mutlitenantsaas.config.TenantContext;
import org.ziad.mutlitenantsaas.config.TenantSchemaResolver;
import org.ziad.mutlitenantsaas.security.config.JwtTokenService;
import org.ziad.mutlitenantsaas.util.Constants;

import java.io.IOException;
import java.util.Collections;

/**
 * Servlet filter that authenticates incoming HTTP requests by validating the JWT
 * Bearer token in the {@code Authorization} header.
 *
 * <p>Executed once per request ({@link OncePerRequestFilter}). For every request
 * except {@code /api/v1/auth/login}:
 * <ol>
 *   <li>Extracts the Bearer token from the {@code Authorization} header.</li>
 *   <li>Validates and parses the token using {@link JwtTokenService}.</li>
 *   <li>Populates {@link TenantContext} with the tenant ID and resolved schema name
 *       so that subsequent Hibernate calls route to the correct PostgreSQL schema.</li>
 *   <li>Builds a {@link UsernamePasswordAuthenticationToken} and stores it in the
 *       {@link SecurityContextHolder} so Spring Security honours the role-based
 *       access checks on controller methods.</li>
 *   <li>Clears {@link TenantContext} after the filter chain completes to prevent
 *       thread-local leakage.</li>
 * </ol>
 *
 * <p>Any authentication error (expired token, malformed token, etc.) is caught and
 * logged; the request continues down the filter chain unauthenticated, which causes
 * Spring Security to return {@code 401 Unauthorized} for protected endpoints.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final TenantSchemaResolver tenantSchemaResolver;

    /** {@inheritDoc} */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().contains("/api/v1/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && this.jwtTokenService.validateToken(jwt)) {
                final String userId = this.jwtTokenService.getUserIdFromToken(jwt);
                final String tenantId = this.jwtTokenService.getTenantIdFromToken(jwt);
                final String role = this.jwtTokenService.getRoleFromToken(jwt);

                if (tenantId != null) {

                    TenantContext.setCurrentTenant(tenantId);
                    final String schemaName = this.tenantSchemaResolver.resolveTenantSchema(tenantId);
                    TenantContext.setCurrentSchema(schemaName);
                }

                // Create authentication token
                final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                final UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                Collections.singletonList(authority)
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("User authenticated for user ID:{}, tenant: {}, role: {}", userId, tenantId, role);
            }
        } catch (final Exception e) {
            log.error("Error authenticating user", e);
        }

        filterChain.doFilter(request, response);

        TenantContext.clear();
    }

    /**
     * Extracts the raw JWT from the {@code Authorization: Bearer <token>} header.
     *
     * @param request the current HTTP request
     * @return the JWT string without the {@code "Bearer "} prefix, or {@code null}
     *         if the header is absent or does not start with {@code "Bearer "}
     */
    private String getJwtFromRequest(final HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(Constants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}