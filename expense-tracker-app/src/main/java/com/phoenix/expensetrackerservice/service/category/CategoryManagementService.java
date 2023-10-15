package com.phoenix.expensetrackerservice.service.category;

import com.phoenix.expensetrackerservice.model.CategoryDTO;

import java.util.List;

public interface CategoryManagementService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO changeCategory(CategoryDTO categoryDTO);

    CategoryDTO retrieveCategory(String categoryId);

    List<CategoryDTO> retrieveCategories();

    void deleteCategory(String categoryId);
}
