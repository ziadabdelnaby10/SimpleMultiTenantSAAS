package org.ziad.mutlitenantsaas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ziad.mutlitenantsaas.entity.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, String> {
    Page<StockMovement> findAllByProduct_Id(String productId, Pageable pageable);
}