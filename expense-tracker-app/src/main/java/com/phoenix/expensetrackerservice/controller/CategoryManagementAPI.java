package com.phoenix.expensetrackerservice.controller;

import com.phoenix.expensetrackerservice.model.CategoryDTO;
import org.springframework.http.ResponseEntity;

public interface CategoryManagementAPI {
    ResponseEntity<CategoryDTO> createCategory(CategoryDTO categoryDTO);
    ResponseEntity<CategoryDTO> retrieveCategory(String categoryId);
    ResponseEntity<CategoryDTO> changeCategory(CategoryDTO categoryDTO);
    ResponseEntity<Void> deleteCategory(String categoryId);
}
