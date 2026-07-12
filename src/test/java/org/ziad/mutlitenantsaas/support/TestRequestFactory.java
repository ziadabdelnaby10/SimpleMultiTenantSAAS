package org.ziad.mutlitenantsaas.support;

import org.ziad.mutlitenantsaas.dto.request.LoginRequest;
import org.ziad.mutlitenantsaas.dto.request.RegisterTenantRequest;
import org.ziad.mutlitenantsaas.dto.request.UserRequest;
import org.ziad.mutlitenantsaas.entity.UserRole;

/**
 * Centralized test DTO builders to keep test setup concise and consistent.
 */
public final class TestRequestFactory {

    private TestRequestFactory() {
    }

    public static LoginRequest validLoginRequest() {
        return LoginRequest.builder()
                .username("john.admin")
                .password("Admin@1234")
                .build();
    }

    public static RegisterTenantRequest validRegisterTenantRequest() {
        return RegisterTenantRequest.builder()
                .companyName("Acme Corporation")
                .companyCode("acme-corp")
                .email("contact@acme.com")
                .adminFullName("John Admin")
                .adminEmail("admin@acme.com")
                .adminUsername("john.admin")
                .adminPassword("Admin@1234")
                .build();
    }

    public static UserRequest validUserRequest() {
        return UserRequest.builder()
                .username("jane.smith")
                .email("jane.smith@acme.com")
                .password("Strong@123")
                .firstName("Jane")
                .lastName("Smith")
                .role(UserRole.ROLE_USER)
                .build();
    }
}

