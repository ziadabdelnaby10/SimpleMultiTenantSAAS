package org.ziad.mutlitenantsaas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ziad.mutlitenantsaas.entity.Product;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link org.ziad.mutlitenantsaas.entity.Product} entities.
 */
public interface ProductRepository extends JpaRepository<Product, String> {

    /**
     * Finds a product by its reference / SKU, ignoring case.
     *
     * @param reference the product reference to search for (case-insensitive)
     * @return an {@link Optional} containing the matching product, if any
     */
    Optional<Product> findByReferenceIgnoreCase(String reference);
}