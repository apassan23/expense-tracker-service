package com.phoenix.expensetrackerservice.service.transaction.impl;

import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteTransactionServiceImplTest {

    private static final String USERNAME = "dummy.user";
    private static final String AUTH_PRINCIPAL = "{\"name\": \"%s\"}".formatted(USERNAME);
    private static final String TRANSACTION_ID = "transactionId";

    @Mock
    private TransactionDataService transactionDataService;

    private DeleteTransactionServiceImpl deleteTransactionService;

    @BeforeEach
    public void setup() {
        deleteTransactionService = spy(new DeleteTransactionServiceImpl(transactionDataService));
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
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> deleteTransactionService.given(transactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, exception.getExpenseError());
    }

    @Test
    public void givenThrowsErrorWhenTransactionDoesNotExistTest() {

        // prepare
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(TRANSACTION_ID);

        setupSecurityContext();

        // mock
        when(transactionDataService.existsByTransactionIdAndUsername(TRANSACTION_ID, USERNAME)).thenReturn(Boolean.FALSE);

        // Action & Assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> deleteTransactionService.given(transactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.TRANSACTION_NOT_PRESENT, exception.getExpenseError());
    }

    @Test
    public void givenTest() {

        // prepare
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(TRANSACTION_ID);

        setupSecurityContext();

        // mock
        when(transactionDataService.existsByTransactionIdAndUsername(TRANSACTION_ID, USERNAME)).thenReturn(Boolean.TRUE);
        doNothing().when(transactionDataService).deleteByTransactionId(TRANSACTION_ID);

        // Action & Assert
        TransactionDTO response = deleteTransactionService.given(transactionDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(TRANSACTION_ID, response.getTransactionId());
    }

    private void setupSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);
    }
}