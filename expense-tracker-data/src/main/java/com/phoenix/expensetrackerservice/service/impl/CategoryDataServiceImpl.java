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
    public Optional<Category> findByCategoryId(String categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Optional<Category> findByTitle(String title) {
        return categoryRepository.findByTitle(title);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteByCategoryId(String categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public boolean existsByCategoryId(String categoryId) {
        return categoryRepository.existsById(categoryId);
    }

}
