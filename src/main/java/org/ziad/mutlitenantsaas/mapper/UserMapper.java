package org.ziad.mutlitenantsaas.mapper;

import org.springframework.stereotype.Component;
import org.ziad.mutlitenantsaas.dto.request.UserRequest;
import org.ziad.mutlitenantsaas.dto.response.UserResponse;
import org.ziad.mutlitenantsaas.entity.Tenant;
import org.ziad.mutlitenantsaas.entity.User;
import org.ziad.mutlitenantsaas.entity.UserRole;
import org.ziad.mutlitenantsaas.util.StringHelper;

@Component
public class UserMapper {

    public User toEntity(final Tenant tenant) {
        return User.builder()
                .username(tenant.getAdminUsername())
                .email(tenant.getAdminEmail())
                .firstName(StringHelper.extractFirstName(tenant.getAdminFullName()))
                .lastName(StringHelper.extractLastName(tenant.getAdminFullName()))
                .role(UserRole.ROLE_COMPANY_ADMIN)
                .password(tenant.getAdminPassword())
                .deleted(false)
                .build();
    }

    public User toEntity(final UserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .deleted(false)
                .build();
    }

    public UserResponse toResponse(final User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}
