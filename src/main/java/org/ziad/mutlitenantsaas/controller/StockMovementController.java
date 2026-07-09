package org.ziad.mutlitenantsaas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ziad.mutlitenantsaas.dto.request.StockMovementRequest;
import org.ziad.mutlitenantsaas.dto.response.StockMovementResponse;
import org.ziad.mutlitenantsaas.service.StockMovementService;

@RestController
@RequestMapping("v1/stocks")
@RequiredArgsConstructor
@Tag(name = "Stock Movement", description = "Stock Movement API")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @PostMapping
    public ResponseEntity<Void> createStockMvt(
            @RequestBody
            @Valid final StockMovementRequest request
    ) {
        this.stockMovementService.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{stock-mvt-id}")
    public ResponseEntity<Void> updateStockMvt(
            @RequestBody
            @Valid final StockMovementRequest request,
            @PathVariable("stock-mvt-id")
            @NotNull(message = "Stock Mvt ID cannot be null") final String id
    ) {
        this.stockMovementService.update(id, request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{stock-mvt-id}")
    public ResponseEntity<StockMovementResponse> findStockMvtById(
            @PathVariable("stock-mvt-id")
            @NotNull(message = "Stock Mvt ID cannot be null") final String id
    ) {
        return ResponseEntity.ok(this.stockMovementService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<StockMovementResponse>> findAllStockMvts(
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.stockMovementService.findAll(pageable));
    }

    @GetMapping("/product/{product-id}")
    public ResponseEntity<Page<StockMovementResponse>> findAllStockMovementsByProductId(
            @PathVariable("product-id")
            @NotNull(message = "Product ID cannot be null") final String productId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.stockMovementService.findAllByProductId(productId, pageable));
    }

    @DeleteMapping("/{stock-mvt-id}")
    public ResponseEntity<Void> deleteStockMvt(
            @PathVariable("stock-mvt-id")
            @NotNull(message = "Stock Mvt ID cannot be null") final String id
    ) {
        this.stockMovementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}