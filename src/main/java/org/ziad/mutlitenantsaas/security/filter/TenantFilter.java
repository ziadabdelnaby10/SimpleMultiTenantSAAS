package org.ziad.mutlitenantsaas.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.ziad.mutlitenantsaas.config.TenantContext;
import org.ziad.mutlitenantsaas.exception.MissingTenantHeaderException;
import org.ziad.mutlitenantsaas.util.Constants;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            final String tenantId = resolveHeader(request);
            TenantContext.setCurrentTenant(tenantId);
            filterChain.doFilter(request, response);
        } catch (MissingTenantHeaderException ex) {
            handleMissingTenantException(response, ex);
        } finally {
            TenantContext.clear();
        }
    }

    private String resolveHeader(final HttpServletRequest request) {
        final String tenantId = request.getHeader(Constants.TENANT_HEADER);
        if (tenantId != null && !tenantId.isBlank()) {
            return tenantId.toLowerCase();
        }
        throw new MissingTenantHeaderException();
    }

    private void handleMissingTenantException(HttpServletResponse response, MissingTenantHeaderException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", Instant.now().toString());
        errorBody.put("status", 400);
        errorBody.put("error", "Missing Required Header");
        errorBody.put("message", ex.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(errorBody));
        response.getWriter().flush();
    }
}
