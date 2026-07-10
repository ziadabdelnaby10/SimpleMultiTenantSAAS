package org.ziad.mutlitenantsaas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import org.ziad.mutlitenantsaas.common.AbstractEntity;

import java.util.Objects;

/**
 * Represents a product category within a tenant's schema.
 *
 * <p>Categories are used to group {@link Product} entities. Each tenant maintains its
 * own set of categories in its dedicated PostgreSQL schema.
 *
 * <p>{@code equals} and {@code hashCode} are implemented following the
 * <em>Hibernate Proxy-safe</em> pattern: two {@code Category} instances are considered
 * equal if and only if both have a non-null {@code id} and those IDs are equal.
 * This avoids equality issues with lazy-loaded proxies.
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends AbstractEntity {

    /** Display name of the category. Must not be {@code null}. */
    @Column(nullable = false)
    private String name;

    /** Optional free-text description of the category. */
    private String description;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(), category.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
