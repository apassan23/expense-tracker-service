package com.phoenix.expensetrackerservice.controller.impl;

import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.category.CategoryManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryManagementControllerTest {

    private static final String CATEGORY_ID = "categoryId";

    @Mock
    private CategoryManagementService categoryManagementService;

    private CategoryManagementController categoryManagementController;

    @BeforeEach
    void setup() {
        categoryManagementController = spy(new CategoryManagementController(categoryManagementService));
    }

    @Test
    void createCategoryTest() {
        // prepare
        CategoryDTO categoryDTO = new CategoryDTO();

        // mock
        when(categoryManagementService.createCategory(categoryDTO)).thenReturn(new CategoryDTO());

        // Action & assert
        categoryManagementController.createCategory(categoryDTO);
    }

    @Test
    void retrieveCategoriesTest() {
        // mock
        when(categoryManagementService.retrieveCategories()).thenReturn(List.of());

        // Action & assert
        categoryManagementController.retrieveCategories();
    }

    @Test
    void retrieveCategoryTest() {
        // mock
        when(categoryManagementService.retrieveCategory(CATEGORY_ID)).thenReturn(new CategoryDTO());

        // Action & assert
        categoryManagementController.retrieveCategory(CATEGORY_ID);
    }

    @Test
    void changeCategoryTest() {
        // prepare
        CategoryDTO categoryDTO = new CategoryDTO();

        // mock
        when(categoryManagementService.changeCategory(categoryDTO)).thenReturn(new CategoryDTO());

        // Action & assert
        categoryManagementController.changeCategory(categoryDTO);
    }

    @Test
    void deleteCategoryTest() {
        // mock
        doNothing().when(categoryManagementService).deleteCategory(CATEGORY_ID);

        // Action & assert
        categoryManagementController.deleteCategory(CATEGORY_ID);
    }
}