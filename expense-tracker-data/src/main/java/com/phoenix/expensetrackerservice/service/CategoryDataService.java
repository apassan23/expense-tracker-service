package com.phoenix.expensetrackerservice.service;

import com.phoenix.expensetrackerservice.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDataService {
    Category save(Category category);

    Optional<Category> findByCategoryId(String categoryId);

    Optional<Category> findByTitle(String title);

    List<Category> findAll();

    void deleteByCategoryId(String categoryId);

    boolean existsByCategoryId(String categoryId);
}
