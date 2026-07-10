package org.ziad.mutlitenantsaas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.ziad.mutlitenantsaas.common.AbstractEntity;

import java.util.Collection;
import java.util.List;

/**
 * Represents an application user belonging to a specific {@link Tenant}.
 *
 * <p>Implements Spring Security's {@link UserDetails} so it can be returned directly
 * from {@link org.ziad.mutlitenantsaas.service.UserService#loadUserByUsername} and used
 * by the authentication pipeline without an additional wrapper.
 *
 * <p>Users are scoped to a tenant: all queries that retrieve or modify a user must
 * also check the {@code tenant_id} foreign key to prevent cross-tenant data leakage.
 * Soft-delete is enforced by the inherited {@code deleted} flag from {@link org.ziad.mutlitenantsaas.common.AbstractEntity}.
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractEntity implements UserDetails {

    /** The tenant this user belongs to. Loaded lazily to avoid N+1 on bulk reads. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", foreignKey = @ForeignKey(name = "fk_user_tenant_id"))
    private Tenant tenant;

    /** Unique login username across the entire platform. */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /** Unique email address across the entire platform. */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /** BCrypt-hashed password. Never returned in API responses. */
    @Column(name = "password", nullable = false)
    private String password;

    /** User's given (first) name. */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /** User's family (last) name. */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /** Role that controls what operations this user may perform. */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    /**
     * Whether the account is currently enabled.
     * Disabled users can authenticate but Spring Security will reject them.
     */
    @Column(name = "enabled")
    private boolean enabled;

    /**
     * Returns a single-element authority list derived from the user's {@link UserRole}.
     *
     * @return unmodifiable list containing one {@link SimpleGrantedAuthority}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    /**
     * Returns the ID of the {@link Tenant} this user belongs to, or {@code null}
     * if the tenant association has not been loaded.
     *
     * @return tenant UUID string, or {@code null}
     */
    public String getTenantId() {
        if (this.tenant != null) {
            return this.tenant.getId();
        }
        return null;
    }
}
