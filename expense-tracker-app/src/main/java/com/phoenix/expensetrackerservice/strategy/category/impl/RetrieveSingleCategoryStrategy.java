package com.phoenix.expensetrackerservice.strategy.category.impl;

import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.CategoryDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.category.RetrieveCategoryStrategy;
import com.phoenix.expensetrackerservice.transform.CategoryEntityBuilder;
import com.phoenix.expensetrackerservice.utils.AuthUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class RetrieveSingleCategoryStrategy implements RetrieveCategoryStrategy {

    private final CategoryDataService categoryDataService;

    public RetrieveSingleCategoryStrategy(CategoryDataService categoryDataService) {
        this.categoryDataService = categoryDataService;
    }

    @Override
    public List<CategoryDTO> retrieve(CategoryDTO categoryDTO) {
        String username = AuthUtils.getUsername();
        if (Objects.isNull(username)) {
            throw new ExpenseTrackerException("Username is null!", ExpenseError.SERVER_ERROR);
        }
        String categoryId = categoryDTO.getCategoryId();
        Optional<Category> categoryOptional = categoryDataService.findByCategoryIdAndUsername(categoryId, username);
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
