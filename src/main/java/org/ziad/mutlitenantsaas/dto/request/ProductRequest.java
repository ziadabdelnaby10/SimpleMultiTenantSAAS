package org.ziad.mutlitenantsaas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Schema(description = "Product creation / update payload")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @Schema(description = "Product name", example = "Wireless Mouse")
    @NotBlank(message = "Product name should not be empty")
    @Size(min = 3, max = 255, message = "Product name should be between 3 and 255 characters")
    private String name;

    @Schema(description = "Unique product reference / SKU", example = "WM-001-BLK")
    @NotBlank(message = "Product reference should not be empty")
    @Size(min = 3, max = 255, message = "Product reference should be between 3 and 255 characters")
    private String reference;

    @Schema(description = "Optional product description", example = "Ergonomic wireless mouse with 2.4 GHz USB receiver")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Schema(description = "Minimum stock quantity before a low-stock alert is triggered", example = "10")
    @NotNull(message = "Alert threshold is required")
    @Positive(message = "Alert threshold should be a positive number")
    private Integer alertThreshold;

    @Schema(description = "Unit price of the product (must be greater than 0)", example = "29.99")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Schema(description = "ID of the category this product belongs to", example = "cat-abc-123")
    @NotBlank(message = "Category ID should not be empty")
    private String categoryId;
}