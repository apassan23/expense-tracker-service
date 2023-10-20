package com.phoenix.expensetrackerservice.service;

import com.phoenix.expensetrackerservice.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDataService {
    Category save(Category category);

    Optional<Category> findByCategoryIdAndUsername(String categoryId, String username);

    Optional<Category> findByTitleAndUsername(String title, String username);

    List<Category> findAllByUsername(String username);

    void deleteByCategoryId(String categoryId);

    boolean existsByCategoryIdAndUsername(String categoryId, String username);
}
