package org.ziad.mutlitenantsaas.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ziad.mutlitenantsaas.dto.request.ProductRequest;
import org.ziad.mutlitenantsaas.dto.response.ProductResponse;
import org.ziad.mutlitenantsaas.service.ProductService;

@RestController
@RequestMapping("v1/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product API")
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<Void> createProduct(
            @RequestBody
            @Valid final ProductRequest request
    ) {
        this.service.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{product-id}")
    public ResponseEntity<Void> updateProduct(
            @RequestBody
            @Valid final ProductRequest request,
            @PathVariable("product-id")
            @NotNull(message = "Product ID cannot be null") final String id
    ) {
        this.service.update(id, request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findProductById(
            @PathVariable("product-id")
            @NotNull(message = "Product ID cannot be null") final String id
    ) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAllProducts(
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.service.findAll(pageable));
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("product-id")
            @NotNull(message = "Product ID cannot be null") final String id
    ) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }
}