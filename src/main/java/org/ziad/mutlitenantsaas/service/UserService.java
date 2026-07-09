package org.ziad.mutlitenantsaas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.ziad.mutlitenantsaas.dto.request.UserRequest;
import org.ziad.mutlitenantsaas.dto.response.UserResponse;
import org.ziad.mutlitenantsaas.entity.Tenant;

public interface UserService extends UserDetailsService {

    void createUser(final UserRequest userRequest);

    void createRoleAdminUser(final Tenant tenant);

    void updateUser(final String id , final UserRequest userRequest);

    void deleteUser(final String id);

    UserResponse getUserById(final String id);

    Page<UserResponse> getUsers(Pageable pageable);

    void enableUser(final String userId);

    void disableUser(final String userId);

    boolean validateUsernameAndEmail(final String username, final String email);

    Page<UserResponse> getAllUsers(Pageable pageable);
}
