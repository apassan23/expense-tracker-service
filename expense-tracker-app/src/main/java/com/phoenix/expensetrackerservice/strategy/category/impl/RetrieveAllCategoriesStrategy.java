package com.phoenix.expensetrackerservice.strategy.category.impl;

import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.CategoryDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.category.RetrieveCategoryStrategy;
import com.phoenix.expensetrackerservice.transform.CategoryEntityBuilder;
import com.phoenix.expensetrackerservice.utils.AuthUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class RetrieveAllCategoriesStrategy implements RetrieveCategoryStrategy {
    private final CategoryDataService categoryDataService;

    public RetrieveAllCategoriesStrategy(CategoryDataService categoryDataService) {
        this.categoryDataService = categoryDataService;
    }

    @Override
    public List<CategoryDTO> retrieve(CategoryDTO categoryDTO) {
        String username = AuthUtils.getUsername();
        if (Objects.isNull(username)) {
            throw new ExpenseTrackerException("Username is null!", ExpenseError.SERVER_ERROR);
        }
        List<Category> categories = categoryDataService.findAllByUsername(username);
        return categories.stream().map(CategoryEntityBuilder::buildFromCategory).toList();
    }

    @Override
    public RetrieveType retrieveType() {
        return RetrieveType.FETCH_ALL;
    }
}
