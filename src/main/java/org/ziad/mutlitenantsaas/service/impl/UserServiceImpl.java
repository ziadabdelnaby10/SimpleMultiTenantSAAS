package org.ziad.mutlitenantsaas.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ziad.mutlitenantsaas.config.TenantContext;
import org.ziad.mutlitenantsaas.dto.request.UserRequest;
import org.ziad.mutlitenantsaas.dto.response.UserResponse;
import org.ziad.mutlitenantsaas.entity.Tenant;
import org.ziad.mutlitenantsaas.entity.User;
import org.ziad.mutlitenantsaas.entity.UserRole;
import org.ziad.mutlitenantsaas.exception.AlreadyExistsException;
import org.ziad.mutlitenantsaas.exception.UnauthorizedException;
import org.ziad.mutlitenantsaas.mapper.UserMapper;
import org.ziad.mutlitenantsaas.repositories.UserRepository;
import org.ziad.mutlitenantsaas.service.PasswordEncryptionService;
import org.ziad.mutlitenantsaas.service.TenantService;
import org.ziad.mutlitenantsaas.service.UserService;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TenantService tenantService;
    private final PasswordEncryptionService passwordEncryptionService;

    @Override
    public void createUser(UserRequest userRequest) {
        final String tenantId = TenantContext.getCurrentTenant();

        if (checkUserNameExists(userRequest.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }

        if (checkEmailExists(userRequest.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }

        if (userRequest.getRole().equals(UserRole.ROLE_PLATFORM_ADMIN)) {
            throw new UnauthorizedException("Platform admin cannot be selected as a role");
        }

        final User user = userMapper.toEntity(userRequest);
        user.setTenant(tenantService.getReferenceById(tenantId));
        user.setPassword(passwordEncryptionService.encryptPassword(userRequest.getPassword()));
    }

    @Override
    @Transactional
    public void createRoleAdminUser(Tenant tenant) {
        if (checkUserNameExists(tenant.getAdminUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }

        if (checkEmailExists(tenant.getAdminEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }

        final User user = userMapper.toEntity(tenant);
        user.setTenant(tenant);
        userRepository.save(user);
    }

    @Override
    public void updateUser(String id, UserRequest userRequest) {
        final String tenantId = TenantContext.getCurrentTenant();

        final User user = userRepository.findUserByIdAndDeletedIsFalseAndTenant_Id(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id + " and tenant: " + tenantId));

//        if(!user.getTenant().getId().equals(tenantId)) {
//            throw new UnauthorizedException("User doesn't belong to tenant " + tenantId);
//        }

        if (checkUserNameExists(userRequest.getUsername()) && !user.getUsername().equals(userRequest.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }

        if (checkEmailExists(userRequest.getEmail()) && !user.getEmail().equals(userRequest.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }

        if (userRequest.getRole().equals(UserRole.ROLE_PLATFORM_ADMIN)) {
            throw new UnauthorizedException("Platform admin cannot be selected as a role");
        }

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        userRepository.save(user);

    }

    @Override
    public void deleteUser(String id) {
        final String tenantId = TenantContext.getCurrentTenant();

        final boolean isUserExist = userRepository.existsUserByTenant(id, tenantId);
        if (!isUserExist)
            throw new EntityNotFoundException("User not found with id: " + id + " and tenant: " + tenantId);

        userRepository.softDeleteUserById(id);

    }

    @Override
    public UserResponse getUserById(String id) {
        final String tenantId = TenantContext.getCurrentTenant();
        return userRepository.findUserByIdAndDeletedIsFalseAndTenant_Id(id, tenantId)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id + " and tenant: " + tenantId));
    }

    @Override
    public Page<UserResponse> getUsers(Pageable pageable) {
        final String tenantId = TenantContext.getCurrentTenant();
        return userRepository.findAllByTenant_Id(tenantId, pageable).map(userMapper::toResponse);
    }

    @Override
    public void enableUser(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();

        final boolean isUserExist = userRepository.existsUserByTenant(userId, tenantId);
        if (!isUserExist)
            throw new EntityNotFoundException("User not found with id: " + userId + " and tenant: " + tenantId);

        userRepository.changeEnablementOfUserById(userId, true);
    }

    @Override
    public void disableUser(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();

        final boolean isUserExist = userRepository.existsUserByTenant(userId, tenantId);
        if (!isUserExist)
            throw new EntityNotFoundException("User not found with id: " + userId + " and tenant: " + tenantId);

        userRepository.changeEnablementOfUserById(userId, false);
    }

    @Override
    public boolean validateUsernameAndEmail(String username, String email) {
        return checkUserNameExists(username) || checkEmailExists(email);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public boolean checkUserNameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
