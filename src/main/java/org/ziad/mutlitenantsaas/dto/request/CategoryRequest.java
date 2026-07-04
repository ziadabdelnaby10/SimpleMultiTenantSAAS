package org.ziad.mutlitenantsaas.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @NotNull(message = "Category name is required")
    private String name;

    private String description;
}
