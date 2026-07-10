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
import org.ziad.mutlitenantsaas.dto.request.ProductRequest;
import org.ziad.mutlitenantsaas.dto.response.ProductResponse;
import org.ziad.mutlitenantsaas.service.ProductService;

@Tag(name = "Product", description = "Product API")
@SecurityRequirement(name = "bearerAuth")
@Validated
@RestController
@RequestMapping("v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create product", description = "Create a new product for the current tenant.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failure", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "409", description = "Product reference already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody @Valid final ProductRequest request) {
        this.productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update product", description = "Update an existing product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Product update accepted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failure", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(
            @RequestBody @Valid final ProductRequest request,
            @PathVariable("productId") @NotBlank(message = "Product ID must not be blank") final String productId
    ) {
        this.productService.update(productId, request);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a single product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findProductById(
            @PathVariable("productId") @NotBlank(message = "Product ID must not be blank") final String productId
    ) {
        return ResponseEntity.ok(this.productService.findById(productId));
    }

    @Operation(summary = "List products", description = "Retrieve a paginated list of all products for the current tenant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of products returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAllProducts(
            @ParameterObject
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.productService.findAll(pageable));
    }

    @Operation(summary = "Delete product", description = "Delete a product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("productId") @NotBlank(message = "Product ID must not be blank") final String productId
    ) {
        this.productService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}