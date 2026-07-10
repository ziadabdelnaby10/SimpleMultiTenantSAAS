package org.ziad.mutlitenantsaas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.ziad.mutlitenantsaas.dto.request.UserRequest;
import org.ziad.mutlitenantsaas.dto.response.UserResponse;
import org.ziad.mutlitenantsaas.entity.Tenant;

/**
 * Service contract for user management within a tenant.
 *
 * <p>Extends {@link UserDetailsService} so Spring Security can load users by username
 * during the authentication flow.
 *
 * <p>All operations that read or mutate users are automatically scoped to the current
 * tenant via {@link org.ziad.mutlitenantsaas.config.TenantContext}.
 */
public interface UserService extends UserDetailsService {

    /**
     * Creates a new user within the current tenant's context.
     *
     * @param userRequest validated user creation payload
     * @throws org.ziad.mutlitenantsaas.exception.AlreadyExistsException if the
     *         username or email is already taken
     * @throws org.ziad.mutlitenantsaas.exception.UnauthorizedException if
     *         {@code ROLE_PLATFORM_ADMIN} is selected as the role
     */
    void createUser(final UserRequest userRequest);

    /**
     * Creates the initial {@code ROLE_COMPANY_ADMIN} user for a newly approved tenant.
     * Called by {@link org.ziad.mutlitenantsaas.event.TenantApprovedListener} after the
     * approval transaction commits.
     *
     * @param tenant the approved tenant whose admin credentials seed this user
     */
    void createRoleAdminUser(final Tenant tenant);

    /**
     * Updates an existing user's profile. The user must belong to the current tenant.
     *
     * @param id          UUID of the user to update
     * @param userRequest validated update payload
     * @throws jakarta.persistence.EntityNotFoundException if the user is not found in
     *         the current tenant
     */
    void updateUser(final String id, final UserRequest userRequest);

    /**
     * Soft-deletes a user (sets {@code deleted = true}). The user must belong to the
     * current tenant.
     *
     * @param id UUID of the user to delete
     * @throws jakarta.persistence.EntityNotFoundException if the user is not found
     */
    void deleteUser(final String id);

    /**
     * Retrieves a user by ID, scoped to the current tenant.
     *
     * @param id UUID of the user
     * @return the user's response DTO
     * @throws jakarta.persistence.EntityNotFoundException if not found
     */
    UserResponse getUserById(final String id);

    /**
     * Returns a paginated list of users for the current tenant (non-deleted only).
     *
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of {@link UserResponse} DTOs
     */
    Page<UserResponse> getUsers(Pageable pageable);

    /**
     * Enables a previously disabled user account within the current tenant.
     *
     * @param userId UUID of the user to enable
     * @throws jakarta.persistence.EntityNotFoundException if the user is not found
     */
    void enableUser(final String userId);

    /**
     * Disables a user account without deleting it, within the current tenant.
     *
     * @param userId UUID of the user to disable
     * @throws jakarta.persistence.EntityNotFoundException if the user is not found
     */
    void disableUser(final String userId);

    /**
     * Checks whether either the given username or email is already in use.
     *
     * @param username the username to check
     * @param email    the email to check
     * @return {@code true} if either the username or email is already registered
     */
    boolean validateUsernameAndEmail(final String username, final String email);

    /**
     * Returns a paginated list of all users across the platform (ignores tenant context).
     * Intended for platform-admin use only.
     *
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of {@link UserResponse} DTOs
     */
    Page<UserResponse> getAllUsers(Pageable pageable);
}
