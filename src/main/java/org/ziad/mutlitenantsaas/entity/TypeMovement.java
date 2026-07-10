package org.ziad.mutlitenantsaas.entity;

/**
 * Direction of a {@link StockMovement}.
 *
 * <ul>
 *   <li>{@link #IN}  — stock received (e.g. purchase order, return from customer).</li>
 *   <li>{@link #OUT} — stock dispatched (e.g. sale, return to supplier).</li>
 * </ul>
 *
 * <p>Available quantity is computed as
 * {@code SUM(quantity WHERE type=IN) - SUM(quantity WHERE type=OUT)}.
 */
public enum TypeMovement {
    /** Stock received into inventory. */
    IN,
    /** Stock dispatched from inventory. */
    OUT
}