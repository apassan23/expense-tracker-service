package com.phoenix.expensetrackerservice.service.transaction.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.service.category.impl.CategoryManagementServiceImpl;
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

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTransactionServiceImplTest {

    private static final String USERNAME = "dummy.user";
    private static final String AUTH_PRINCIPAL = "{\"name\": \"%s\"}".formatted(USERNAME);
    private static final String TRANSACTION_NAME = "TRANSACTION";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String CATEGORY_ID = "categoryId";
    private static final String REQUEST_CATEGORY_ID = "requestCategoryId";

    @Mock
    private TransactionDataService transactionDataService;
    @Mock
    private CategoryManagementServiceImpl categoryManagementService;

    private CreateTransactionServiceImpl createTransactionService;

    @BeforeEach
    public void setup() {
        createTransactionService = spy(new CreateTransactionServiceImpl(transactionDataService, categoryManagementService));
    }

    @AfterEach
    public void cleanup() {
        // clear security context after each test
        SecurityContextHolder.clearContext();
    }

    @Test
    public void givenWhenUsernameNullTest() {

        // prepare
        TransactionDTO transactionDTO = new TransactionDTO();

        // Action & Assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> createTransactionService.given(transactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, exception.getExpenseError());
    }

    @Test
    public void givenThrowsErrorWhenTransactionNotFoundTest() {

        // prepare
        Transaction transaction = new Transaction();

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionName(TRANSACTION_NAME);

        setupSecurityContext();

        // mock
        when(transactionDataService.findByUsernameAndTransactionName(USERNAME, TRANSACTION_NAME)).thenReturn(Optional.of(transaction));

        // Action & Assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> createTransactionService.given(transactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.TRANSACTION_CREATE_ALREADY_EXISTS, exception.getExpenseError());

        verify(transactionDataService, times(0)).save(any(Transaction.class));
        verify(categoryManagementService, times(0)).retrieveCategory(anyString());
    }

    @Test
    public void givenTest() {

        // prepare
        Date todayDate = new Date();

        Transaction transaction = new Transaction();
        transaction.setTransactionName(TRANSACTION_NAME);
        transaction.setUsername(USERNAME);
        transaction.setCategoryId(CATEGORY_ID);
        transaction.setTransactionDate(todayDate);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionName(TRANSACTION_NAME);
        transactionDTO.setCategoryId(REQUEST_CATEGORY_ID);

        setupSecurityContext();

        // mock
        when(transactionDataService.findByUsernameAndTransactionName(USERNAME, TRANSACTION_NAME)).thenReturn(Optional.empty());
        when(transactionDataService.save(any(Transaction.class))).thenReturn(transaction);
        when(categoryManagementService.retrieveCategory(REQUEST_CATEGORY_ID)).thenReturn(new CategoryDTO());

        // Action & assert
        TransactionDTO response = createTransactionService.given(transactionDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(TRANSACTION_NAME, response.getTransactionName());
        Assertions.assertEquals(CATEGORY_ID, response.getCategoryId());
        Assertions.assertEquals(todayDate, response.getTransactionDate());

        verify(categoryManagementService, times(1)).retrieveCategory(anyString());
    }

    @Test
    public void givenThrowsErrorWhenCategoryNotFoundTest() {

        // prepare
        Date todayDate = new Date();

        Transaction transaction = new Transaction();
        transaction.setCategoryId(CATEGORY_ID);
        transaction.setTransactionDate(todayDate);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionName(TRANSACTION_NAME);
        transactionDTO.setCategoryId(CATEGORY_ID);

        setupSecurityContext();

        // mock
        when(transactionDataService.findByUsernameAndTransactionName(USERNAME, TRANSACTION_NAME)).thenReturn(Optional.empty());
        when(categoryManagementService.retrieveCategory(CATEGORY_ID)).thenReturn(null);

        // Action & Assert
        ExpenseTrackerNotFoundException exception = Assertions.assertThrows(ExpenseTrackerNotFoundException.class, () -> createTransactionService.given(transactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.CATEGORY_DOES_NOT_EXISTS, exception.getExpenseError());
    }

    private void setupSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);
    }
}