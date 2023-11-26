package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryDataServiceImplTest {

    private static final String CATEGORY_ID = "categoryId";
    private static final String USERNAME = "username";

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryDataServiceImpl categoryDataService;

    @BeforeEach
    void setup() {
        categoryDataService = spy(new CategoryDataServiceImpl(categoryRepository));
    }

    @Test
    void saveTest() {
        // prepare
        Category category = new Category();

        // mock
        when(categoryRepository.save(category)).thenReturn(category);

        // Action & assert
        Category response = categoryDataService.save(category);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(category, response);
    }

    @Test
    void findByCategoryIdAndUsernameTest() {
        // prepare
        Category category = new Category();

        // mock
        when(categoryRepository.findByCategoryIdAndUsername(CATEGORY_ID, USERNAME)).thenReturn(Optional.of(category));

        // Action & assert
        Optional<Category> response = categoryDataService.findByCategoryIdAndUsername(CATEGORY_ID, USERNAME);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(category, response.get());
    }

    @Test
    void findByTitleAndUsernameTest() {
        // prepare
        final String CATEGORY_TITLE = "categoryId";
        Category category = new Category();

        // mock
        when(categoryRepository.findByTitleAndUsername(CATEGORY_TITLE, USERNAME)).thenReturn(Optional.of(category));

        // Action & assert
        Optional<Category> response = categoryDataService.findByTitleAndUsername(CATEGORY_TITLE, USERNAME);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals(category, response.get());
    }

    @Test
    void findAllByUsernameTest() {
        // prepare
        List<Category> categories = List.of();

        // mock
        when(categoryRepository.findAllByUsername(USERNAME)).thenReturn(categories);

        // Action & assert
        List<Category> response = categoryDataService.findAllByUsername(USERNAME);
        Assertions.assertNotNull(response);
        Assertions.assertIterableEquals(categories, response);
    }

    @Test
    void deleteByCategoryIdTest() {
        // mock
        doNothing().when(categoryRepository).deleteById(CATEGORY_ID);

        // Action & assert
        Assertions.assertDoesNotThrow(() -> categoryDataService.deleteByCategoryId(CATEGORY_ID));
    }

    @Test
    void existsByCategoryIdAndUsernameTest() {
        // mock
        when(categoryRepository.existsByCategoryIdAndUsername(CATEGORY_ID, USERNAME)).thenReturn(Boolean.TRUE);

        // Action & assert
        boolean result = categoryDataService.existsByCategoryIdAndUsername(CATEGORY_ID, USERNAME);
        Assertions.assertTrue(result);
    }
}