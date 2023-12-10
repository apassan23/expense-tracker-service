package com.phoenix.expensetrackerservice.service.category.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.CategoryDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.category.RetrieveCategoryStrategy;
import com.phoenix.expensetrackerservice.strategy.category.factory.RetrieveCategoryStrategyFactory;
import com.phoenix.expensetrackerservice.transform.CategoryEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryManagementServiceImplTest {

    private static final String USERNAME = "dummy.user";
    private static final String CATEGORY_TITLE = "categoryTitle";
    private static final String REQUEST_CATEGORY_TITLE = "requestCategoryTitle";
    private static final String CATEGORY_ID = "categoryID";
    private static final String AUTH_PRINCIPAL = "{\"name\": \"%s\"}".formatted(USERNAME);
    private static final List<CategoryDTO> CATEGORIES;

    static {
        try {
            CATEGORIES = getCategoriesFromFile("src/test/resources/data/Categories.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Mock
    private CategoryDataService categoryDataService;
    @Mock
    private CategoryRequestValidationServiceImpl categoryRequestValidationService;
    @Mock
    private RetrieveCategoryStrategyFactory retrieveCategoryStrategyFactory;

    private CategoryManagementServiceImpl categoryManagementService;

    @BeforeEach
    void setup() {
        categoryManagementService = spy(new CategoryManagementServiceImpl(categoryDataService, categoryRequestValidationService, retrieveCategoryStrategyFactory));
    }

    @AfterEach
    void cleanup() {
        // clear security context after each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void createCategoryTest() {
        // prepare
        CategoryDTO categoryDTO = generateCategoryRequest();
        Category category = CategoryEntityBuilder.build(USERNAME, categoryDTO);

        // setup security context
        setupSecurityContext();

        // mock
        doNothing().when(categoryRequestValidationService).validateForCreate(categoryDTO);
        when(categoryDataService.save(any(Category.class))).thenReturn(category);

        // Action & assert
        CategoryDTO response = categoryManagementService.createCategory(categoryDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(categoryDTO.getCategoryId(), response.getCategoryId());
        Assertions.assertEquals(categoryDTO.getTitle(), response.getTitle());
        Assertions.assertEquals(categoryDTO.getDescription(), response.getDescription());
        Assertions.assertEquals(categoryDTO.getGroup(), response.getGroup());
    }

    @Test
    void createCategoryWhenUsernameNullTest() {
        // prepare
        CategoryDTO categoryDTO = generateCategoryRequest();

        // Action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> categoryManagementService.createCategory(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ErrorConstants.USERNAME_NULL_MESSAGE, exception.getMessage());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, exception.getExpenseError());
    }

    @Test
    void createCategoryWhenCategoryExistsTest() {
        // prepare
        CategoryDTO categoryDTO = generateCategoryRequest();
        Category category = CategoryEntityBuilder.build(USERNAME, categoryDTO);

        setupSecurityContext();

        // mock
        when(categoryDataService.findByTitleAndUsername(CATEGORY_TITLE, USERNAME)).thenReturn(Optional.of(category));

        // Action & assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> categoryManagementService.createCategory(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.CATEGORY_ALREADY_EXISTS.getDescription(), exception.getMessage());
        Assertions.assertEquals(ExpenseError.CATEGORY_ALREADY_EXISTS, exception.getExpenseError());

        verify(categoryDataService, times(0)).save(any());
    }

    @Test
    void changeCategoryTest() {
        // prepare
        CategoryDTO categoryDTO = generateCategoryRequest();
        Category category = CategoryEntityBuilder.build(USERNAME, categoryDTO);

        setupSecurityContext();

        // mock
        doNothing().when(categoryRequestValidationService).validateForChange(categoryDTO);
        when(categoryDataService.findByCategoryIdAndUsername(CATEGORY_ID, USERNAME)).thenReturn(Optional.of(category));
        when(categoryDataService.save(category)).thenReturn(category);

        // Action & assert
        CategoryDTO response = categoryManagementService.changeCategory(categoryDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(categoryDTO.getCategoryId(), response.getCategoryId());
        Assertions.assertEquals(categoryDTO.getTitle(), response.getTitle());
        Assertions.assertEquals(categoryDTO.getDescription(), response.getDescription());
        Assertions.assertEquals(categoryDTO.getGroup(), response.getGroup());
    }

    @Test
    void changeCategoryWhenUsernameNullTest() {
        // prepare
        CategoryDTO categoryDTO = generateCategoryRequest();

        // Action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> categoryManagementService.changeCategory(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ErrorConstants.USERNAME_NULL_MESSAGE, exception.getMessage());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, exception.getExpenseError());
    }

    @Test
    void changeCategoryWhenCategoryDoesNotExistTest() {
        // prepare
        CategoryDTO categoryDTO = generateCategoryRequest();

        setupSecurityContext();

        // mock
        doNothing().when(categoryRequestValidationService).validateForChange(categoryDTO);
        when(categoryDataService.findByCategoryIdAndUsername(CATEGORY_ID, USERNAME)).thenReturn(Optional.empty());

        // Action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> categoryManagementService.changeCategory(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), exception.getMessage());
        Assertions.assertEquals(ExpenseError.CATEGORY_DOES_NOT_EXISTS, exception.getExpenseError());

        verify(categoryDataService, times(0)).save(any());
    }

    @Test
    void changeCategoryWhenCategoryAlreadyExistsTest() {
        // prepare
        CategoryDTO categoryDTO = generateCategoryRequest();
        Category category = CategoryEntityBuilder.build(USERNAME, categoryDTO);

        categoryDTO.setTitle(REQUEST_CATEGORY_TITLE);

        setupSecurityContext();

        // mock
        doNothing().when(categoryRequestValidationService).validateForChange(categoryDTO);
        when(categoryDataService.findByCategoryIdAndUsername(CATEGORY_ID, USERNAME)).thenReturn(Optional.of(category));
        when(categoryDataService.findByTitleAndUsername(REQUEST_CATEGORY_TITLE, USERNAME)).thenReturn(Optional.of(category));

        // Action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> categoryManagementService.changeCategory(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ErrorConstants.CATEGORY_ALREADY_EXISTS_MESSAGE, exception.getMessage());
        Assertions.assertEquals(ExpenseError.CATEGORY_ALREADY_EXISTS, exception.getExpenseError());

        verify(categoryDataService, times(0)).save(any());
    }

    @Test
    void retrieveCategoryTest() {
        // prepare
        RetrieveCategoryStrategy retrieveCategoryStrategy = mock(RetrieveCategoryStrategy.class);

        // mock
        doNothing().when(categoryRequestValidationService).validateForRetrieve(any(CategoryDTO.class));
        when(retrieveCategoryStrategyFactory.getStrategy(RetrieveType.FETCH_SINGLE_ENTITY)).thenReturn(retrieveCategoryStrategy);
        when(retrieveCategoryStrategy.retrieve(any(CategoryDTO.class))).thenReturn(CATEGORIES);

        // Action & assert
        CategoryDTO response = categoryManagementService.retrieveCategory(CATEGORY_ID);
        Assertions.assertNotNull(response);
    }

    @Test
    void retrieveCategoriesTest() {
        // prepare
        RetrieveCategoryStrategy retrieveCategoryStrategy = mock(RetrieveCategoryStrategy.class);

        // mock
        when(retrieveCategoryStrategyFactory.getStrategy(RetrieveType.FETCH_ALL)).thenReturn(retrieveCategoryStrategy);
        when(retrieveCategoryStrategy.retrieve(any(CategoryDTO.class))).thenReturn(CATEGORIES);

        // Action & assert
        List<CategoryDTO> response = categoryManagementService.retrieveCategories();
        Assertions.assertNotNull(response);
        Assertions.assertIterableEquals(CATEGORIES, response);
    }

    @Test
    void deleteCategoryTest() {
        // prepare
        setupSecurityContext();

        // mock
        doNothing().when(categoryRequestValidationService).validateForDelete(any(CategoryDTO.class));
        when(categoryDataService.existsByCategoryIdAndUsername(CATEGORY_ID, USERNAME)).thenReturn(Boolean.TRUE);

        // Action & assert
        Assertions.assertDoesNotThrow(() -> categoryManagementService.deleteCategory(CATEGORY_ID));
    }

    @Test
    void deleteCategoryWhenUsernameNullTest() {
        // Action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> categoryManagementService.deleteCategory(CATEGORY_ID));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ErrorConstants.USERNAME_NULL_MESSAGE, exception.getMessage());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, exception.getExpenseError());
    }

    @Test
    void deleteCategoryWhenCategoryDoesNotExistsTest() {
        // prepare
        setupSecurityContext();

        // mock
        doNothing().when(categoryRequestValidationService).validateForDelete(any(CategoryDTO.class));
        when(categoryDataService.existsByCategoryIdAndUsername(CATEGORY_ID, USERNAME)).thenReturn(Boolean.FALSE);

        // Action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> categoryManagementService.deleteCategory(CATEGORY_ID));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), exception.getMessage());
        Assertions.assertEquals(ExpenseError.CATEGORY_DOES_NOT_EXISTS, exception.getExpenseError());

        verify(categoryDataService, times(0)).deleteByCategoryId(any());
    }

    private void setupSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);
    }

    private static List<CategoryDTO> getCategoriesFromFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String json = FileUtils.readFileToString(new File(path), "UTF-8");

        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }


    private static CategoryDTO generateCategoryRequest() {
        return CategoryDTO.builder()
                .categoryId(CATEGORY_ID)
                .title(CATEGORY_TITLE)
                .group("categoryGroup")
                .description("categoryDescription")
                .build();
    }
}