package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.CategoryDataService;
import com.phoenix.expensetrackerservice.service.CategoryManagementService;
import com.phoenix.expensetrackerservice.service.CategoryRequestValidationService;
import com.phoenix.expensetrackerservice.transform.CategoryBuilder;
import com.phoenix.expensetrackerservice.transform.CategoryEntityBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryManagementServiceImpl implements CategoryManagementService {
    private final CategoryDataService categoryDataService;
    private final CategoryRequestValidationService categoryRequestValidationService;

    public CategoryManagementServiceImpl(CategoryDataService categoryDataService, CategoryRequestValidationService categoryRequestValidationService) {
        this.categoryDataService = categoryDataService;
        this.categoryRequestValidationService = categoryRequestValidationService;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        categoryRequestValidationService.validateForCreate(categoryDTO);
        String categoryTitle = categoryDTO.getTitle();
        Optional<Category> categoryOptional = categoryDataService.findByTitle(categoryTitle);
        if(categoryOptional.isPresent()) {
            throw new ExpenseTrackerBadRequestException(ExpenseError.CATEGORY_ALREADY_EXISTS.getDescription(), ExpenseError.CATEGORY_ALREADY_EXISTS);
        }
        Category category = CategoryEntityBuilder.buildFromCategoryDTO(categoryDTO);
        return CategoryEntityBuilder.buildFromCategory(categoryDataService.save(category));
    }

    @Override
    public CategoryDTO changeCategory(CategoryDTO categoryDTO) {
        categoryRequestValidationService.validateForChange(categoryDTO);
        String categoryId = categoryDTO.getCategoryId();
        Optional<Category> categoryOptional = categoryDataService.findByCategoryId(categoryId);
        if(categoryOptional.isEmpty()) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), ExpenseError.CATEGORY_DOES_NOT_EXISTS);
        }
        Category retrievedCategory = categoryOptional.get();
        if(!retrievedCategory.getTitle().equals(categoryDTO.getTitle())) {
            Optional<Category> byTitle = categoryDataService.findByTitle(categoryDTO.getTitle());
            if(byTitle.isPresent()) {
                // category name change is requested but a category with the same name already exists
                throw new ExpenseTrackerBadRequestException(ErrorConstants.CATEGORY_ALREADY_EXISTS_MESSAGE, ExpenseError.CATEGORY_ALREADY_EXISTS);
            }
        }
        Category category = CategoryEntityBuilder.buildFromCategoryDTO(categoryDTO);
        return CategoryEntityBuilder.buildFromCategory(categoryDataService.save(category));
    }

    @Override
    public CategoryDTO retrieveCategory(String categoryId) {
        CategoryDTO categoryDTO = CategoryBuilder.buildFromCategoryId(categoryId);
        categoryRequestValidationService.validateForRetrieve(categoryDTO);
        Optional<Category> categoryOptional = categoryDataService.findByCategoryId(categoryId);
        if(categoryOptional.isEmpty()) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), ExpenseError.CATEGORY_DOES_NOT_EXISTS);
        }
        return CategoryEntityBuilder.buildFromCategory(categoryOptional.get());
    }

    @Override
    public void deleteCategory(String categoryId) {
        CategoryDTO categoryDTO = CategoryBuilder.buildFromCategoryId(categoryId);
        categoryRequestValidationService.validateForRetrieve(categoryDTO);
        if(!categoryDataService.existsByCategoryId(categoryId)) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), ExpenseError.CATEGORY_DOES_NOT_EXISTS);
        }
        categoryDataService.deleteByCategoryId(categoryId);
    }
}
