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
import org.ziad.mutlitenantsaas.dto.request.CategoryRequest;
import org.ziad.mutlitenantsaas.dto.response.CategoryResponse;
import org.ziad.mutlitenantsaas.service.CategoryService;

@Tag(name = "Category", description = "Category management API")
@SecurityRequirement(name = "bearerAuth")
@Validated
@RestController
@RequestMapping("v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create category", description = "Create a new product category for the current tenant.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failure", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized — JWT missing or invalid", content = @Content),
            @ApiResponse(responseCode = "409", description = "Category name already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.create(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "List categories", description = "Retrieve a paginated list of all categories for the current tenant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of categories returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getCategories(
            @ParameterObject
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(categoryService.findAll(pageable));
    }

    @Operation(summary = "Update category", description = "Update the name or description of an existing category.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failure", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable @NotBlank(message = "Category ID must not be blank") String categoryId,
            @Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.update(categoryId, categoryRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a single category by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable @NotBlank(message = "Category ID must not be blank") String categoryId) {
        return ResponseEntity.ok(categoryService.findById(categoryId));
    }

    @Operation(summary = "Delete category", description = "Delete a category by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Category has linked products and cannot be deleted", content = @Content)
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable @NotBlank(message = "Category ID must not be blank") String categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }
}
