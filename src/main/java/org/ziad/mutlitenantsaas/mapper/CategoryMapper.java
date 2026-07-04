package org.ziad.mutlitenantsaas.mapper;

import org.springframework.stereotype.Service;
import org.ziad.mutlitenantsaas.dto.request.CategoryRequest;
import org.ziad.mutlitenantsaas.dto.response.CategoryResponse;
import org.ziad.mutlitenantsaas.entity.Category;

@Service
public class CategoryMapper {

    public Category toEntity(final CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .deleted(false)
                .build();
    }

    public CategoryResponse toResponse(final Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
