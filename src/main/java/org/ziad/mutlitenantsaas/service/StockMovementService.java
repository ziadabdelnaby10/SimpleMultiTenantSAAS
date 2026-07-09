package org.ziad.mutlitenantsaas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ziad.mutlitenantsaas.dto.request.StockMovementRequest;
import org.ziad.mutlitenantsaas.dto.response.StockMovementResponse;

public interface StockMovementService extends BasicService<StockMovementRequest, StockMovementResponse> {
    Page<StockMovementResponse> findAllByProductId(final String productId, final Pageable pageable);
}
