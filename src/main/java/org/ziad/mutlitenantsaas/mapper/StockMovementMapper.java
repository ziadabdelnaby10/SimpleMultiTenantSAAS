package org.ziad.mutlitenantsaas.mapper;

import org.springframework.stereotype.Component;
import org.ziad.mutlitenantsaas.dto.request.StockMovementRequest;
import org.ziad.mutlitenantsaas.dto.response.StockMovementResponse;
import org.ziad.mutlitenantsaas.entity.Product;
import org.ziad.mutlitenantsaas.entity.StockMovement;

@Component
public class StockMovementMapper {
    public StockMovement toEntity(final StockMovementRequest request) {
        return StockMovement.builder()
                .dateMovement(request.getDateMovement())
                .comment(request.getComment())
                .typeMovement(request.getTypeMovement())
                .quantity(request.getQuantity())
                .product(Product.builder()
                        .id(request.getProductId())
                        .build())
                .deleted(false)
                .build();
    }

    public StockMovementResponse toResponse(final StockMovement entity) {
        return StockMovementResponse.builder()
                .id(entity.getId())
                .dateMovement(entity.getDateMovement())
                .comment(entity.getComment())
                .typeMovement(entity.getTypeMovement())
                .quantity(entity.getQuantity())
                .build();
    }
}
