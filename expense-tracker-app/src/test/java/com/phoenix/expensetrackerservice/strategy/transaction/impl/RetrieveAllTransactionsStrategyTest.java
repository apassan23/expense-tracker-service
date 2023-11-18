package com.phoenix.expensetrackerservice.strategy.transaction.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveAllTransactionsStrategyTest {

    private static final String USERNAME = "dummy.user";
    private static final String AUTH_PRINCIPAL = "{\"name\": \"%s\"}".formatted(USERNAME);

    @Mock
    private TransactionDataService transactionDataService;

    private RetrieveAllTransactionsStrategy retrieveAllTransactionsStrategy;

    @BeforeEach
    void setup() {
        retrieveAllTransactionsStrategy = spy(new RetrieveAllTransactionsStrategy(transactionDataService));
    }

    @Test
    void retrieveTest() {
        // prepare
        final int LENGTH = 5;
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        List<Transaction> transactions = getTransactions(LENGTH);
        SecurityContextHolder.setContext(mockSecurityContext);

        // stub
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);
        when(transactionDataService.findAllByUsername(USERNAME)).thenReturn(transactions);

        // action & assert
        List<TransactionDTO> transactionDTOS = retrieveAllTransactionsStrategy.retrieve(new RetrieveTransactionDTO());
        Assertions.assertNotNull(transactionDTOS);
        Assertions.assertEquals(LENGTH, transactionDTOS.size());
        verify(transactionDataService, times(1)).findAllByUsername(USERNAME);
    }

    @Test
    void retrieveWithNoTransactionsTest() {
        // prepare
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        SecurityContextHolder.setContext(mockSecurityContext);

        // stub
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);

        // action & assert
        List<TransactionDTO> transactionDTOS = retrieveAllTransactionsStrategy.retrieve(new RetrieveTransactionDTO());
        Assertions.assertNotNull(transactionDTOS);
        Assertions.assertEquals(0, transactionDTOS.size());
        verify(transactionDataService, times(1)).findAllByUsername(USERNAME);
    }

    @Test
    void retrieveWithNullUsernameTest() {
        // prepare
        SecurityContextHolder.clearContext();

        // action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> retrieveAllTransactionsStrategy.retrieve(new RetrieveTransactionDTO()));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals("Username is null!", exception.getMessage());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, exception.getExpenseError());
    }

    private static List<Transaction> getTransactions(int length) {
        return IntStream.range(1, length + 1)
                .mapToObj(idx -> new Transaction())
                .toList();
    }

    @Test
    void retrieveTypeTest() {
        // action & assert
        RetrieveType retrieveType = retrieveAllTransactionsStrategy.retrieveType();
        Assertions.assertEquals(RetrieveType.FETCH_ALL, retrieveType);
    }
}