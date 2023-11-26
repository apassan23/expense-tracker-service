package com.phoenix.expensetrackerservice.strategy.transaction.impl;

import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
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
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveTransactionByPageStrategyTest {

    private static final String USERNAME = "dummy.user";
    private static final String AUTH_PRINCIPAL = "{\"name\": \"%s\"}".formatted(USERNAME);

    @Mock
    private TransactionDataService transactionDataService;
    private RetrieveTransactionByPageStrategy retrieveTransactionByPageStrategy;

    @BeforeEach
    void setup() {
        retrieveTransactionByPageStrategy = spy(new RetrieveTransactionByPageStrategy(transactionDataService));
    }

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void retrieveTest() {
        // prepare
        final int LENGTH = 5;
        final String DATE = "1999-02-23";
        final Integer PAGE_NUMBER = 1;
        RetrieveTransactionDTO retrieveTransactionDTO = new RetrieveTransactionDTO();
        retrieveTransactionDTO.setDate(DATE);
        retrieveTransactionDTO.setPageNumber(PAGE_NUMBER);
        retrieveTransactionDTO.setPageSize(LENGTH);
        List<Transaction> transactions = getTransactions(LENGTH);

        setupSecurityContext();

        // stub
        when(transactionDataService.findAllByUsernameAndDate(eq(USERNAME), any(Date.class), eq(PAGE_NUMBER), eq(LENGTH))).thenReturn(transactions);

        // action & assert
        List<TransactionDTO> transactionDTOS = retrieveTransactionByPageStrategy.retrieve(retrieveTransactionDTO);
        Assertions.assertNotNull(transactionDTOS);
        Assertions.assertEquals(LENGTH, transactionDTOS.size());
        verify(transactionDataService, times(1)).findAllByUsernameAndDate(anyString(), any(), anyInt(), anyInt());
    }

    @Test
    void retrieveWithNullUsernameTest() {
        SecurityContextHolder.clearContext();

        // action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> retrieveTransactionByPageStrategy.retrieve(new RetrieveTransactionDTO()));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals("Error: %s".formatted(ErrorConstants.USERNAME_NULL_MESSAGE), exception.getMessage());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, exception.getExpenseError());
    }

    @Test
    void retrieveThrowsErrorTest() {
        // prepare
        final int LENGTH = 5;
        final String DATE = "23-22-1";
        final Integer PAGE_NUMBER = 1;
        RetrieveTransactionDTO retrieveTransactionDTO = new RetrieveTransactionDTO();
        retrieveTransactionDTO.setDate(DATE);
        retrieveTransactionDTO.setPageNumber(PAGE_NUMBER);
        retrieveTransactionDTO.setPageSize(LENGTH);

        setupSecurityContext();

        // action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> retrieveTransactionByPageStrategy.retrieve(retrieveTransactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
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
        RetrieveType retrieveType = retrieveTransactionByPageStrategy.retrieveType();
        Assertions.assertEquals(RetrieveType.FETCH_BY_PAGE, retrieveType);
    }

    private void setupSecurityContext() {
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);

        SecurityContextHolder.setContext(mockSecurityContext);

        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);
    }
}