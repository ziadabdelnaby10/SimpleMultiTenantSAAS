package org.ziad.mutlitenantsaas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ziad.mutlitenantsaas.dto.request.StockMovementRequest;
import org.ziad.mutlitenantsaas.dto.response.StockMovementResponse;
import org.ziad.mutlitenantsaas.service.StockMovementService;

@Tag(name = "Stock Movement", description = "Stock Movement API")
@SecurityRequirement(name = "bearerAuth")
@Validated
@RestController
@RequestMapping("v1/stocks")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @Operation(summary = "Create stock movement", description = "Record a new IN or OUT stock movement for a product.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Stock movement created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failure", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> createStockMvt(@RequestBody @Valid final StockMovementRequest request) {
        this.stockMovementService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update stock movement", description = "Update an existing stock movement record.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Stock movement update accepted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failure", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Stock movement not found", content = @Content)
    })
    @PutMapping("/{stockId}")
    public ResponseEntity<Void> updateStockMvt(
            @RequestBody @Valid final StockMovementRequest request,
            @PathVariable("stockId") @NotBlank(message = "Stock movement ID must not be blank") final String stockId
    ) {
        this.stockMovementService.update(stockId, request);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Get stock movement by ID", description = "Retrieve a single stock movement record by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock movement found",
                    content = @Content(schema = @Schema(implementation = StockMovementResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Stock movement not found", content = @Content)
    })
    @GetMapping("/{stockId}")
    public ResponseEntity<StockMovementResponse> findStockMvtById(
            @PathVariable("stockId") @NotBlank(message = "Stock movement ID must not be blank") final String stockId
    ) {
        return ResponseEntity.ok(this.stockMovementService.findById(stockId));
    }

    @Operation(summary = "List stock movements", description = "Retrieve a paginated list of all stock movements for the current tenant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of stock movements returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<StockMovementResponse>> findAllStockMvts(
            @ParameterObject
            @PageableDefault(size = 20, sort = "dateMovement", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.stockMovementService.findAll(pageable));
    }

    @Operation(summary = "List stock movements by product", description = "Retrieve a paginated list of stock movements filtered by a specific product.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of stock movements for the product"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<StockMovementResponse>> findAllStockMovementsByProductId(
            @PathVariable("productId") @NotBlank(message = "Product ID must not be blank") final String productId,
            @ParameterObject
            @PageableDefault(size = 20, sort = "dateMovement", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.stockMovementService.findAllByProductId(productId, pageable));
    }

    @Operation(summary = "Delete stock movement", description = "Delete a stock movement record by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Stock movement deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Stock movement not found", content = @Content)
    })
    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> deleteStockMvt(
            @PathVariable("stockId") @NotBlank(message = "Stock movement ID must not be blank") final String stockId
    ) {
        this.stockMovementService.delete(stockId);
        return ResponseEntity.noContent().build();
    }
}