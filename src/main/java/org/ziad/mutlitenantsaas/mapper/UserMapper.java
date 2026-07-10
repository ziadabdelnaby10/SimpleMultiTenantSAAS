package org.ziad.mutlitenantsaas.mapper;

import org.springframework.stereotype.Component;
import org.ziad.mutlitenantsaas.dto.request.UserRequest;
import org.ziad.mutlitenantsaas.dto.response.UserResponse;
import org.ziad.mutlitenantsaas.entity.Tenant;
import org.ziad.mutlitenantsaas.entity.User;
import org.ziad.mutlitenantsaas.entity.UserRole;
import org.ziad.mutlitenantsaas.util.StringHelper;

/**
 * Converts between {@link User} entity and its associated DTOs.
 */
@Component
public class UserMapper {

    /**
     * Creates a {@link User} entity pre-populated with the admin credentials stored
     * on a {@link Tenant}. Used by the tenant approval flow to seed the initial
     * {@code ROLE_COMPANY_ADMIN} account.
     *
     * <p>First and last name are extracted from {@link Tenant#getAdminFullName()} via
     * {@link org.ziad.mutlitenantsaas.util.StringHelper}.
     *
     * @param tenant the approved tenant whose admin credentials seed the new user
     * @return a new, unpersisted {@link User} entity with {@code role = ROLE_COMPANY_ADMIN}
     */
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

    /**
     * Maps a {@link UserRequest} DTO to a new {@link User} entity.
     * Password hashing and tenant association must be set by the caller after mapping.
     *
     * @param request the validated user creation / update payload
     * @return a new, unpersisted {@link User} entity
     */
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

    /**
     * Maps a persisted {@link User} entity to a {@link UserResponse} DTO.
     * Sensitive fields such as {@code password} are intentionally excluded.
     *
     * @param user the entity to convert
     * @return the corresponding response DTO
     */
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
