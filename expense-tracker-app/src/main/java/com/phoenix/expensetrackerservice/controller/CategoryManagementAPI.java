package com.phoenix.expensetrackerservice.controller;

import com.phoenix.expensetrackerservice.model.CategoryDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryManagementAPI {
    ResponseEntity<CategoryDTO> createCategory(CategoryDTO categoryDTO);

    ResponseEntity<CategoryDTO> retrieveCategory(String categoryId);

    ResponseEntity<List<CategoryDTO>> retrieveCategories();

    ResponseEntity<CategoryDTO> changeCategory(CategoryDTO categoryDTO);

    ResponseEntity<Void> deleteCategory(String categoryId);
}
