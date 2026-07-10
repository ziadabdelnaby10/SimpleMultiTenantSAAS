package org.ziad.mutlitenantsaas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class WebConfig {

    /**
     * Caps the maximum page size a client can request to 100.
     * Without this, a client could pass ?size=999999 and trigger
     * an unbounded database query.
     */
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableResolverCustomizer() {
        return (PageableHandlerMethodArgumentResolver resolver) -> resolver.setMaxPageSize(100);
    }
}

