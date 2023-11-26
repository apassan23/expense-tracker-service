package com.phoenix.expensetrackerservice.strategy.category.impl;

import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
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
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveAllCategoriesStrategyTest {

    private static final String username = "dummy.user";
    private static final String AUTH_PRINCIPAL = "{\"name\": \"%s\"}".formatted(username);

    @Mock
    private CategoryDataService categoryDataService;

    private RetrieveAllCategoriesStrategy retrieveAllCategoriesStrategy;

    @BeforeEach
    void setup() {
        retrieveAllCategoriesStrategy = spy(new RetrieveAllCategoriesStrategy(categoryDataService));
    }

    @Test
    void retrieveTest() {
        // prepare
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        List<Category> categories = getCategories(5);
        SecurityContextHolder.setContext(mockSecurityContext);

        // stub
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);
        when(categoryDataService.findAllByUsername(username)).thenReturn(categories);

        // action & assert
        List<CategoryDTO> categoryDTOS = retrieveAllCategoriesStrategy.retrieve(new CategoryDTO());
        Assertions.assertNotNull(categoryDTOS);
        Assertions.assertEquals(5, categoryDTOS.size());
    }

    @Test
    void retrieveThrowsServerExceptionTest() {
        // prepare
        SecurityContextHolder.clearContext();
        
        // action & assert
        Assertions.assertThrows(ExpenseTrackerException.class, () -> retrieveAllCategoriesStrategy.retrieve(new CategoryDTO()));
    }

    private List<Category> getCategories(int length) {
        return IntStream.range(1, length + 1)
                .mapToObj(idx ->
                        new Category(
                                "categoryId%s".formatted(idx),
                                "username%s".formatted(idx),
                                "title%s".formatted(idx),
                                "description",
                                "group%s".formatted(idx)
                        ))
                .toList();
    }

    @Test
    void retrieveTypeTest() {
        // action & assert
        RetrieveType retrieveType = retrieveAllCategoriesStrategy.retrieveType();
        Assertions.assertEquals(RetrieveType.FETCH_ALL, retrieveType);
    }
}