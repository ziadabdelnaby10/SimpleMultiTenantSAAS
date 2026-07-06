package org.ziad.mutlitenantsaas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.ziad.mutlitenantsaas.entity.TypeMovement;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMovementRequest {
    private TypeMovement typeMovement;
    @Positive(message = "Quantity should be a positive number")
    private Integer quantity;

    private Instant dateMovement;

    private String comment;
    @NotBlank(message = "Product ID should not be empty")
    private String productId;
}
