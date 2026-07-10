package org.ziad.mutlitenantsaas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.ziad.mutlitenantsaas.entity.TypeMovement;

import java.time.Instant;

@Schema(description = "Stock movement record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMovementResponse {

    @Schema(description = "Unique movement identifier", accessMode = Schema.AccessMode.READ_ONLY, example = "mvt-001")
    private String id;

    @Schema(description = "Movement type: IN (received) or OUT (dispatched)", accessMode = Schema.AccessMode.READ_ONLY, example = "IN")
    private TypeMovement typeMovement;

    @Schema(description = "Number of units moved", accessMode = Schema.AccessMode.READ_ONLY, example = "50")
    private Integer quantity;

    @Schema(description = "Date and time of the movement in UTC", accessMode = Schema.AccessMode.READ_ONLY, example = "2026-07-10T14:30:00Z")
    private Instant dateMovement;

    @Schema(description = "Optional note attached to this movement", accessMode = Schema.AccessMode.READ_ONLY, example = "Received from supplier PO-9981")
    private String comment;
}
