package org.ziad.mutlitenantsaas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Category details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    @Schema(description = "Unique category identifier", accessMode = Schema.AccessMode.READ_ONLY, example = "cat-abc-123")
    private String id;

    @Schema(description = "Category name", accessMode = Schema.AccessMode.READ_ONLY, example = "Electronics")
    private String name;

    @Schema(description = "Category description", accessMode = Schema.AccessMode.READ_ONLY, example = "Consumer electronics and accessories")
    private String description;
}
