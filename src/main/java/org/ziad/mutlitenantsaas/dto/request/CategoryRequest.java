package org.ziad.mutlitenantsaas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "Category creation / update payload")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    @Schema(description = "Unique name of the category", example = "Electronics")
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Optional description of the category", example = "Consumer electronics and accessories")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
