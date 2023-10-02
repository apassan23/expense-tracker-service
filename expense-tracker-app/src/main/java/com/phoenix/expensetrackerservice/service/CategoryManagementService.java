package com.phoenix.expensetrackerservice.service;

import com.phoenix.expensetrackerservice.model.CategoryDTO;

public interface CategoryManagementService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO changeCategory(CategoryDTO categoryDTO);
    CategoryDTO retrieveCategory(String categoryId);
    void deleteCategory(String categoryId);
}
