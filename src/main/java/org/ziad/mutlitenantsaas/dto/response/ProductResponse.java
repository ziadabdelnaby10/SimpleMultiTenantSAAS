package org.ziad.mutlitenantsaas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Schema(description = "Product details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    @Schema(description = "Unique product identifier", accessMode = Schema.AccessMode.READ_ONLY, example = "prod-xyz-456")
    private String id;

    @Schema(description = "Product name", accessMode = Schema.AccessMode.READ_ONLY, example = "Wireless Mouse")
    private String name;

    @Schema(description = "Product reference / SKU", accessMode = Schema.AccessMode.READ_ONLY, example = "WM-001-BLK")
    private String reference;

    @Schema(description = "Product description", accessMode = Schema.AccessMode.READ_ONLY, example = "Ergonomic wireless mouse with 2.4 GHz USB receiver")
    private String description;

    @Schema(description = "Low-stock alert threshold quantity", accessMode = Schema.AccessMode.READ_ONLY, example = "10")
    private Integer alertThreshold;

    @Schema(description = "Unit price", accessMode = Schema.AccessMode.READ_ONLY, example = "29.99")
    private BigDecimal price;

    @Schema(description = "ID of the product's category", accessMode = Schema.AccessMode.READ_ONLY, example = "cat-abc-123")
    private String categoryId;

    @Schema(description = "Name of the product's category", accessMode = Schema.AccessMode.READ_ONLY, example = "Electronics")
    private String categoryName;

    @Schema(description = "Current available stock quantity (IN movements minus OUT movements)", accessMode = Schema.AccessMode.READ_ONLY, example = "150")
    private int availableQuantity;
}