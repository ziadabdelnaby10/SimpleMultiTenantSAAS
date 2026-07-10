package org.ziad.mutlitenantsaas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ziad.mutlitenantsaas.entity.StockMovement;

/**
 * Spring Data JPA repository for {@link org.ziad.mutlitenantsaas.entity.StockMovement} entities.
 */
public interface StockMovementRepository extends JpaRepository<StockMovement, String> {

    /**
     * Returns a paginated list of stock movements for the given product.
     *
     * @param productId UUID of the product
     * @param pageable  pagination and sorting parameters
     * @return a {@link Page} of {@link StockMovement} entities
     */
    Page<StockMovement> findAllByProduct_Id(String productId, Pageable pageable);
}