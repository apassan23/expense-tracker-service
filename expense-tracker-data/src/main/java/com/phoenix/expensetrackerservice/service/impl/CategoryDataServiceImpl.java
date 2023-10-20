package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.repository.CategoryRepository;
import com.phoenix.expensetrackerservice.service.CategoryDataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryDataServiceImpl implements CategoryDataService {
    private final CategoryRepository categoryRepository;

    public CategoryDataServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findByCategoryIdAndUsername(String categoryId, String username) {
        return categoryRepository.findByCategoryIdAndUsername(categoryId, username);
    }

    @Override
    public Optional<Category> findByTitleAndUsername(String title, String username) {
        return categoryRepository.findByTitleAndUsername(title, username);
    }

    @Override
    public List<Category> findAllByUsername(String username) {
        return categoryRepository.findAllByUsername(username);
    }

    @Override
    public void deleteByCategoryId(String categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public boolean existsByCategoryIdAndUsername(String categoryId, String username) {
        return categoryRepository.existsByCategoryIdAndUsername(categoryId, username);
    }

}
