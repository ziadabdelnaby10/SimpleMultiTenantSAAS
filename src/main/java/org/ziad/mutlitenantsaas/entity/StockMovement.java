package org.ziad.mutlitenantsaas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import org.ziad.mutlitenantsaas.common.AbstractEntity;

import java.time.Instant;
import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;

/**
 * Records a single inventory movement (receipt or dispatch) for a {@link Product}.
 *
 * <p>Stock movements are immutable audit records: every change to available quantity is
 * represented as a new row rather than an update to the product. Available quantity is
 * calculated as {@code SUM(quantity WHERE typeMovement=IN) - SUM(quantity WHERE typeMovement=OUT)}.
 *
 * <p>{@code equals}/{@code hashCode} follow the Hibernate Proxy-safe ID-based pattern.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "stock_mvts")
public class StockMovement extends AbstractEntity {

    /** Direction of the movement — {@code IN} for receipts, {@code OUT} for dispatches. */
    @Column(name = "type_mvt", nullable = false)
    @Enumerated(STRING)
    private TypeMovement typeMovement;

    /** Number of units moved in this transaction. Must be a positive integer. */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /** Date and time at which the physical movement occurred, stored in UTC. */
    @Column(name = "date_mvt", nullable = false)
    private Instant dateMovement;

    /** Optional free-text note explaining the reason for this movement. */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /** The product whose stock level is affected by this movement. */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        StockMovement stockMovement = (StockMovement) o;
        return getId() != null && Objects.equals(getId(), stockMovement.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}