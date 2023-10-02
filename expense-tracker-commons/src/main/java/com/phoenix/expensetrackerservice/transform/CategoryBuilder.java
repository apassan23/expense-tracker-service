package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.model.CategoryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryBuilder {

    public static CategoryDTO buildFromCategoryId(String categoryId) {
        return CategoryDTO.builder()
                .categoryId(categoryId)
                .build();
    }
}
