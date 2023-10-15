package com.phoenix.expensetrackerservice.strategy.impl;

import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.CategoryDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveCategoryStrategy;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.transform.CategoryEntityBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RetrieveAllCategoriesStrategy implements RetrieveCategoryStrategy {
    private final CategoryDataService categoryDataService;

    public RetrieveAllCategoriesStrategy(CategoryDataService categoryDataService) {
        this.categoryDataService = categoryDataService;
    }

    @Override
    public List<CategoryDTO> retrieve(CategoryDTO categoryDTO) {
        List<Category> categories = categoryDataService.findAll();
        return categories.stream().map(CategoryEntityBuilder::buildFromCategory).toList();
    }

    @Override
    public RetrieveType retrieveType() {
        return RetrieveType.FETCH_ALL;
    }
}
