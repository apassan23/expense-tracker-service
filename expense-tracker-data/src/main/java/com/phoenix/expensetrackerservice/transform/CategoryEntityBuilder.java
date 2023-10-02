package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryEntityBuilder {

    public static Category buildFromCategoryDTO(CategoryDTO categoryDTO) {
        return Category.builder()
                .categoryId(categoryDTO.getCategoryId())
                .group(categoryDTO.getGroup())
                .title(categoryDTO.getTitle())
                .description(categoryDTO.getDescription())
                .build();
    }

    public static CategoryDTO buildFromCategory(Category category) {
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .group(category.getGroup())
                .title(category.getTitle())
                .description(category.getDescription())
                .build();
    }
}
