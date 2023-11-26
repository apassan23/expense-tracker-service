package com.phoenix.expensetrackerservice.controller.impl;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.transaction.TransactionManagementService;
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
class TransactionManagementControllerTest {

    private static final String TRANSACTION_ID = "transactionId";

    @Mock
    private TransactionManagementService transactionManagementService;

    private TransactionManagementController transactionManagementController;

    @BeforeEach
    void setup() {
        transactionManagementController = spy(new TransactionManagementController(transactionManagementService));
    }

    @Test
    void createTransactionTest() {
        // prepare
        TransactionDTO transactionDTO = new TransactionDTO();

        // mock
        when(transactionManagementService.createTransaction(transactionDTO)).thenReturn(new TransactionDTO());

        // Action & assert
        transactionManagementController.createTransaction(transactionDTO);
    }

    @Test
    void retrieveTransactionTest() {
        // mock
        when(transactionManagementService.retrieveTransaction(TRANSACTION_ID)).thenReturn(new TransactionDTO());

        // Action & assert
        transactionManagementController.retrieveTransaction(TRANSACTION_ID);
    }

    @Test
    void retrieveTransactionsTest() {
        // prepare
        RetrieveTransactionDTO transactionDTO = new RetrieveTransactionDTO();

        // mock
        when(transactionManagementService.retrieveTransactions(transactionDTO)).thenReturn(List.of());

        // Action & assert
        transactionManagementController.retrieveTransaction(transactionDTO);
    }

    @Test
    void changeTransactionTest() {
        // prepare
        TransactionDTO transactionDTO = new TransactionDTO();

        // mock
        when(transactionManagementService.changeTransaction(transactionDTO)).thenReturn(new TransactionDTO());

        // Action & assert
        transactionManagementController.changeTransaction(transactionDTO);
    }

    @Test
    void deleteTransactionTest() {

        // mock
        doNothing().when(transactionManagementService).deleteTransaction(TRANSACTION_ID);
        
        // Action & assert
        transactionManagementController.deleteTransaction(TRANSACTION_ID);
    }
}