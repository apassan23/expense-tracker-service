package com.phoenix.expensetrackerservice.strategy.category.impl;

import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.CategoryDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveSingleCategoryStrategyTest {

    private static final String username = "dummy.user";
    private static final String AUTH_PRINCIPAL = "{\"name\": \"%s\"}".formatted(username);
    private static final String CATEGORY_ID = "categoryId";

    RetrieveSingleCategoryStrategy retrieveSingleCategoryStrategy;

    @Mock
    private CategoryDataService categoryDataService;

    @BeforeEach
    void setup() {
        retrieveSingleCategoryStrategy = spy(new RetrieveSingleCategoryStrategy(categoryDataService));
    }

    @Test
    void retrieveTest() {
        // prepare
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        Category category = getCategory();
        CategoryDTO categoryDTO = CategoryDTO.builder().categoryId(CATEGORY_ID).build();
        SecurityContextHolder.setContext(mockSecurityContext);

        // stub
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);
        when(categoryDataService.findByCategoryIdAndUsername(CATEGORY_ID, username)).thenReturn(Optional.of(category));

        // action & assert
        List<CategoryDTO> categoryDTOS = retrieveSingleCategoryStrategy.retrieve(categoryDTO);
        Assertions.assertNotNull(categoryDTOS);
        Assertions.assertEquals(1, categoryDTOS.size());
        Assertions.assertEquals(CATEGORY_ID, categoryDTOS.stream().findFirst().orElseThrow().getCategoryId());
    }

    @Test
    void retrieveWhenUsernameNullTest() {
        CategoryDTO categoryDTO = CategoryDTO.builder().categoryId(CATEGORY_ID).build();

        Exception exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> retrieveSingleCategoryStrategy.retrieve(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals(ErrorConstants.USERNAME_NULL_MESSAGE, exception.getMessage());
    }

    @Test
    void retrieveCategoryDoesNotExistsTest() {
        // prepare
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        CategoryDTO categoryDTO = CategoryDTO.builder().categoryId(CATEGORY_ID).build();
        SecurityContextHolder.setContext(mockSecurityContext);

        // stub
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);
        when(categoryDataService.findByCategoryIdAndUsername(CATEGORY_ID, username)).thenReturn(Optional.empty());

        // action & assert
        Exception exception = Assertions.assertThrows(ExpenseTrackerNotFoundException.class, () -> retrieveSingleCategoryStrategy.retrieve(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), exception.getMessage());
    }

    private static Category getCategory() {
        return new Category(
                CATEGORY_ID,
                "username",
                "title",
                "description",
                "group"
        );
    }

    @Test
    void retrieveTypeTest() {
        // action & assert
        RetrieveType retrieveType = retrieveSingleCategoryStrategy.retrieveType();
        Assertions.assertEquals(RetrieveType.FETCH_SINGLE_ENTITY, retrieveType);
    }
}