package com.phoenix.expensetrackerservice.strategy.impl;

import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.CategoryDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveCategoryStrategy;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.transform.CategoryEntityBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class RetrieveSingleCategoryStrategy implements RetrieveCategoryStrategy {

    private final CategoryDataService categoryDataService;

    public RetrieveSingleCategoryStrategy(CategoryDataService categoryDataService) {
        this.categoryDataService = categoryDataService;
    }

    @Override
    public List<CategoryDTO> retrieve(CategoryDTO categoryDTO) {
        String categoryId = categoryDTO.getCategoryId();
        Optional<Category> categoryOptional = categoryDataService.findByCategoryId(categoryId);
        return categoryOptional
                .map(CategoryEntityBuilder::buildFromCategory)
                .map(Collections::singletonList)
                .orElseThrow(() -> new ExpenseTrackerNotFoundException(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), ExpenseError.CATEGORY_DOES_NOT_EXISTS));
    }

    @Override
    public RetrieveType retrieveType() {
        return RetrieveType.FETCH_SINGLE_ENTITY;
    }
}
