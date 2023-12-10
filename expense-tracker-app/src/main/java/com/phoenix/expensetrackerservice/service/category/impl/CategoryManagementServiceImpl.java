package com.phoenix.expensetrackerservice.service.category.impl;

import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.CategoryDataService;
import com.phoenix.expensetrackerservice.service.category.CategoryManagementService;
import com.phoenix.expensetrackerservice.service.category.CategoryRequestValidationService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.category.RetrieveCategoryStrategy;
import com.phoenix.expensetrackerservice.strategy.category.factory.RetrieveCategoryStrategyFactory;
import com.phoenix.expensetrackerservice.transform.CategoryBuilder;
import com.phoenix.expensetrackerservice.transform.CategoryEntityBuilder;
import com.phoenix.expensetrackerservice.utils.AuthUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryManagementServiceImpl implements CategoryManagementService {
    private final CategoryDataService categoryDataService;
    private final CategoryRequestValidationService categoryRequestValidationService;
    private final RetrieveCategoryStrategyFactory retrieveCategoryStrategyFactory;

    public CategoryManagementServiceImpl(CategoryDataService categoryDataService, CategoryRequestValidationService categoryRequestValidationService, RetrieveCategoryStrategyFactory retrieveCategoryStrategyFactory) {
        this.categoryDataService = categoryDataService;
        this.categoryRequestValidationService = categoryRequestValidationService;
        this.retrieveCategoryStrategyFactory = retrieveCategoryStrategyFactory;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        categoryRequestValidationService.validateForCreate(categoryDTO);
        String username = AuthUtils.getUsername();
        if (Objects.isNull(username)) {
            throw new ExpenseTrackerException(ErrorConstants.USERNAME_NULL_MESSAGE, ExpenseError.SERVER_ERROR);
        }
        String categoryTitle = categoryDTO.getTitle();
        Optional<Category> categoryOptional = categoryDataService.findByTitleAndUsername(categoryTitle, username);
        if (categoryOptional.isPresent()) {
            throw new ExpenseTrackerBadRequestException(ExpenseError.CATEGORY_ALREADY_EXISTS.getDescription(), ExpenseError.CATEGORY_ALREADY_EXISTS);
        }
        Category category = CategoryEntityBuilder.build(username, categoryDTO);
        return CategoryEntityBuilder.buildFromCategory(categoryDataService.save(category));
    }

    @Override
    public CategoryDTO changeCategory(CategoryDTO categoryDTO) {
        categoryRequestValidationService.validateForChange(categoryDTO);
        String categoryId = categoryDTO.getCategoryId();
        String username = AuthUtils.getUsername();
        if (Objects.isNull(username)) {
            throw new ExpenseTrackerException(ErrorConstants.USERNAME_NULL_MESSAGE, ExpenseError.SERVER_ERROR);
        }
        Optional<Category> categoryOptional = categoryDataService.findByCategoryIdAndUsername(categoryId, username);
        if (categoryOptional.isEmpty()) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), ExpenseError.CATEGORY_DOES_NOT_EXISTS);
        }
        Category retrievedCategory = categoryOptional.get();
        if (!retrievedCategory.getTitle().equals(categoryDTO.getTitle())) {
            Optional<Category> byTitle = categoryDataService.findByTitleAndUsername(categoryDTO.getTitle(), username);
            if (byTitle.isPresent()) {
                // category name change is requested but a category with the same name already exists
                throw new ExpenseTrackerBadRequestException(ErrorConstants.CATEGORY_ALREADY_EXISTS_MESSAGE, ExpenseError.CATEGORY_ALREADY_EXISTS);
            }
        }
        Category category = CategoryEntityBuilder.build(username, categoryDTO);
        return CategoryEntityBuilder.buildFromCategory(categoryDataService.save(category));
    }

    @Override
    public CategoryDTO retrieveCategory(String categoryId) {
        CategoryDTO categoryDTO = CategoryBuilder.buildFromCategoryId(categoryId);
        categoryRequestValidationService.validateForRetrieve(categoryDTO);
        RetrieveCategoryStrategy retrieveCategoryStrategy = retrieveCategoryStrategyFactory.getStrategy(RetrieveType.FETCH_SINGLE_ENTITY);
        List<CategoryDTO> categories = retrieveCategoryStrategy.retrieve(categoryDTO);
        return categories.stream().findFirst().orElse(null);
    }

    @Override
    public List<CategoryDTO> retrieveCategories() {
        CategoryDTO categoryDTO = new CategoryDTO();
        RetrieveCategoryStrategy retrieveCategoryStrategy = retrieveCategoryStrategyFactory.getStrategy(RetrieveType.FETCH_ALL);
        return retrieveCategoryStrategy.retrieve(categoryDTO);
    }

    @Override
    public void deleteCategory(String categoryId) {
        String username = AuthUtils.getUsername();
        if (Objects.isNull(username)) {
            throw new ExpenseTrackerException(ErrorConstants.USERNAME_NULL_MESSAGE, ExpenseError.SERVER_ERROR);
        }
        CategoryDTO categoryDTO = CategoryBuilder.buildFromCategoryId(categoryId);
        categoryRequestValidationService.validateForDelete(categoryDTO);
        if (!categoryDataService.existsByCategoryIdAndUsername(categoryId, username)) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), ExpenseError.CATEGORY_DOES_NOT_EXISTS);
        }
        categoryDataService.deleteByCategoryId(categoryId);
    }
}
