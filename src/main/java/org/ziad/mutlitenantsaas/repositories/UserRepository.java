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

public interface UserRepository extends JpaRepository<User, String> {


    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findUserByUsername(String username);

    @EntityGraph(attributePaths = {"tenant"})
    @Query("select u from User u where u.id = ?1 and u.deleted = false and u.tenant.id = ?2")
    Optional<User> findUserByIdAndDeletedIsFalseAndTenant_Id(String id, String tenantId);

    @EntityGraph(attributePaths = {"tenant"})
    @Query("select (count(u) > 0) from User u  where u.id=?1 and u.tenant.id = ?2 and u.deleted = false")
    Boolean existsUserByTenant(String userId, String tenantId);

    @Transactional
    @Modifying
    @Query("update User u set u.deleted = true where u.id = ?1")
    void softDeleteUserById(String id);

    @Transactional
    @Modifying
    @Query("update User u set u.enabled = ?2 where u.id = ?1")
    void changeEnablementOfUserById(String id , boolean enabled);

    @Query("select u from User u where u.tenant.id = ?1 and u.deleted = false")
    @EntityGraph(attributePaths = {"tenant"})
    Page<User> findAllByTenant_Id(String tenantId, Pageable pageable);
}