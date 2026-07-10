package org.ziad.mutlitenantsaas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.ziad.mutlitenantsaas.entity.User;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link User} entities.
 *
 * <p>Extends {@link JpaRepository} with custom queries scoped to the current tenant.
 * All custom queries that access user data must filter by {@code tenant_id} to prevent
 * cross-tenant data leakage.
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Checks whether a user with the given username exists (across all tenants).
     *
     * @param username the username to look up
     * @return {@code true} if at least one user has this username
     */
    boolean existsByUsername(String username);

    /**
     * Checks whether a user with the given email exists (across all tenants).
     *
     * @param email the email address to look up
     * @return {@code true} if at least one user has this email
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by username, eagerly loading the tenant association.
     * Used by the Spring Security {@code loadUserByUsername} flow.
     *
     * @param username the login username
     * @return an {@link Optional} containing the user if found
     */
    Optional<User> findUserByUsername(String username);

    /**
     * Finds a non-deleted user by ID that belongs to the given tenant.
     * Uses an entity graph to eagerly fetch the {@code tenant} association.
     *
     * @param id       UUID of the user
     * @param tenantId UUID of the owning tenant
     * @return an {@link Optional} containing the user if found and not soft-deleted
     */
    @EntityGraph(attributePaths = {"tenant"})
    @Query("select u from User u where u.id = ?1 and u.deleted = false and u.tenant.id = ?2")
    Optional<User> findUserByIdAndDeletedIsFalseAndTenant_Id(String id, String tenantId);

    /**
     * Checks whether a non-deleted user with the given ID belongs to the given tenant.
     *
     * @param userId   UUID of the user
     * @param tenantId UUID of the owning tenant
     * @return {@code true} if the user exists, belongs to the tenant, and is not deleted
     */
    @EntityGraph(attributePaths = {"tenant"})
    @Query("select (count(u) > 0) from User u  where u.id=?1 and u.tenant.id = ?2 and u.deleted = false")
    Boolean existsUserByTenant(String userId, String tenantId);

    /**
     * Soft-deletes a user by setting {@code deleted = true}. Does not issue a physical
     * {@code DELETE}.
     *
     * @param id UUID of the user to soft-delete
     */
    @Transactional
    @Modifying
    @Query("update User u set u.deleted = true where u.id = ?1")
    void softDeleteUserById(String id);

    /**
     * Enables or disables a user account.
     *
     * @param id      UUID of the user
     * @param enabled {@code true} to enable, {@code false} to disable
     */
    @Transactional
    @Modifying
    @Query("update User u set u.enabled = ?2 where u.id = ?1")
    void changeEnablementOfUserById(String id, boolean enabled);

    /**
     * Returns a paginated list of non-deleted users belonging to the given tenant.
     *
     * @param tenantId UUID of the tenant
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of {@link User} entities with their tenant loaded
     */
    @Query("select u from User u where u.tenant.id = ?1 and u.deleted = false")
    @EntityGraph(attributePaths = {"tenant"})
    Page<User> findAllByTenant_Id(String tenantId, Pageable pageable);
}