package org.ziad.mutlitenantsaas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.ziad.mutlitenantsaas.entity.TypeMovement;

import java.time.Instant;

@Schema(description = "Stock movement record payload")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMovementRequest {

    @Schema(description = "Direction of the movement: IN (stock received) or OUT (stock dispatched)", example = "IN")
    @NotNull(message = "Movement type is required")
    private TypeMovement typeMovement;

    @Schema(description = "Number of units moved (must be a positive integer)", example = "50")
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity should be a positive number")
    private Integer quantity;

    @Schema(description = "Date and time of the movement in ISO-8601 UTC format (defaults to now if omitted)", example = "2026-07-10T14:30:00Z")
    @PastOrPresent(message = "Movement date must not be in the future")
    private Instant dateMovement;

    @Schema(description = "Optional note about this movement", example = "Received from supplier PO-9981")
    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String comment;

    @Schema(description = "ID of the product this movement is for", example = "prod-xyz-456")
    @NotBlank(message = "Product ID should not be empty")
    private String productId;
}
