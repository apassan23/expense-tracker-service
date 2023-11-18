package com.phoenix.expensetrackerservice.strategy.transaction.impl;

import com.phoenix.expensetrackerservice.constants.ErrorConstants;
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
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveTransactionStrategyTest {

    private static final String USERNAME = "dummy.user";
    private static final String AUTH_PRINCIPAL = "{\"name\": \"%s\"}".formatted(USERNAME);
    private static final String TRANSACTION_ID = "transactionId";

    @Mock
    private TransactionDataService transactionDataService;
    private RetrieveTransactionStrategy retrieveTransactionStrategy;

    @BeforeEach
    void setup() {
        retrieveTransactionStrategy = spy(new RetrieveTransactionStrategy(transactionDataService));
    }

    @Test
    void retrieveTest() {
        // prepare
        final RetrieveTransactionDTO retrieveTransactionDTO = new RetrieveTransactionDTO();
        retrieveTransactionDTO.setTransactionId(TRANSACTION_ID);
        final Transaction transaction = new Transaction();
        transaction.setTransactionId(TRANSACTION_ID);

        // setup security context
        setupSecurityContext();

        // stub
        when(transactionDataService.findByTransactionIdAndUsername(TRANSACTION_ID, USERNAME)).thenReturn(Optional.of(transaction));

        // action & assert
        List<TransactionDTO> transactionDTOS = retrieveTransactionStrategy.retrieve(retrieveTransactionDTO);
        Assertions.assertNotNull(transactionDTOS);
        Assertions.assertEquals(1, transactionDTOS.size());
        Assertions.assertEquals(TRANSACTION_ID, transactionDTOS.stream().findAny().get().getTransactionId());
    }

    @Test
    void retrieveWithNullUsernameTest() {
        // prepare
        final RetrieveTransactionDTO retrieveTransactionDTO = new RetrieveTransactionDTO();
        retrieveTransactionDTO.setTransactionId(TRANSACTION_ID);

        // clear security context
        SecurityContextHolder.clearContext();

        // action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> retrieveTransactionStrategy.retrieve(new RetrieveTransactionDTO()));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals(ErrorConstants.USERNAME_NULL_MESSAGE, exception.getMessage());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, exception.getExpenseError());
    }

    @Test
    void retrieveWithNoTransactionTest() {
        // prepare
        final RetrieveTransactionDTO retrieveTransactionDTO = new RetrieveTransactionDTO();
        retrieveTransactionDTO.setTransactionId(TRANSACTION_ID);

        // setup security context
        setupSecurityContext();


        // action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> retrieveTransactionStrategy.retrieve(new RetrieveTransactionDTO()));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals(ExpenseError.TRANSACTION_NOT_PRESENT.getDescription(), exception.getMessage());
        Assertions.assertEquals(ExpenseError.TRANSACTION_NOT_PRESENT, exception.getExpenseError());
    }

    @Test
    void retrieveTypeTest() {
        // action & assert
        RetrieveType retrieveType = retrieveTransactionStrategy.retrieveType();
        Assertions.assertEquals(RetrieveType.FETCH_SINGLE_ENTITY, retrieveType);
    }

    private void setupSecurityContext() {
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        SecurityContextHolder.setContext(mockSecurityContext);

        // stub
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);
    }
}