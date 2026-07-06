package org.ziad.mutlitenantsaas.dto.response;

import lombok.*;
import org.ziad.mutlitenantsaas.entity.TypeMovement;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMovementResponse {
    private String id;
    private TypeMovement typeMovement;
    private Integer quantity;
    private Instant dateMovement;
    private String comment;
}
