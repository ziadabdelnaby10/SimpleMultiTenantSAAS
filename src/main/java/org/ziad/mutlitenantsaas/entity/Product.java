package org.ziad.mutlitenantsaas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import org.ziad.mutlitenantsaas.common.AbstractEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Represents a physical product tracked within a tenant's inventory.
 *
 * <p>Each product belongs to exactly one {@link Category} and has a unique
 * {@code reference} (SKU) within the tenant schema. The current available
 * quantity is derived at runtime by summing associated {@link StockMovement} records
 * (IN minus OUT) rather than being stored directly on the product row.
 *
 * <p>{@code equals}/{@code hashCode} follow the Hibernate Proxy-safe ID-based pattern.
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends AbstractEntity {
    /** Human-readable product name. */
    @Column(name = "name", nullable = false)
    private String name;

    /** Unique product reference / SKU within the tenant schema. */
    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    /** Optional long-form product description stored as TEXT. */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Minimum stock quantity that triggers a low-stock alert.
     * When available quantity falls at or below this threshold the product
     * is considered under-stocked.
     */
    @Column(name = "alert_threshold", nullable = false)
    private Integer alertThreshold;

    /** Unit price of the product. */
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /** Category this product belongs to. */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /** All stock movements (IN and OUT) recorded against this product. */
    @OneToMany(mappedBy = "product")
    private List<StockMovement> stockMovements;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
