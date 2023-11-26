package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionDataServiceImplTest {

    private static final String TRANSACTION_ID = "transactionId";
    private static final String USERNAME = "username";

    @Mock
    private MongoOperations mongoOperations;
    @Mock
    private TransactionRepository transactionRepository;

    private TransactionDataServiceImpl transactionDataService;

    @BeforeEach
    void setup() {
        transactionDataService = spy(new TransactionDataServiceImpl(transactionRepository, mongoOperations));
    }

    @Test
    void saveTest() {
        // prepare
        Transaction transaction = new Transaction();

        // mock
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // Action & assert
        Transaction response = transactionDataService.save(transaction);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(transaction, response);
    }

    @Test
    void existsByTransactionIdAndUsernameTest() {
        // mock
        when(transactionRepository.existsByTransactionIdAndUsername(TRANSACTION_ID, USERNAME)).thenReturn(Boolean.TRUE);

        // Action & assert
        boolean result = transactionDataService.existsByTransactionIdAndUsername(TRANSACTION_ID, USERNAME);
        Assertions.assertTrue(result);
    }

    @Test
    void findByTransactionIdAndUsernameTest() {
        // prepare
        Transaction transaction = new Transaction();

        // mock
        when(transactionRepository.findByTransactionIdAndUsername(TRANSACTION_ID, USERNAME)).thenReturn(Optional.of(transaction));

        // Action & assert
        Optional<Transaction> transactionOptional = transactionDataService.findByTransactionIdAndUsername(TRANSACTION_ID, USERNAME);
        Assertions.assertNotNull(transactionOptional);
        Assertions.assertTrue(transactionOptional.isPresent());
        Assertions.assertEquals(transaction, transactionOptional.get());
    }

    @Test
    void findByUsernameAndTransactionNameTest() {
        // prepare
        final String TRANSACTION_NAME = "transactionName";
        Transaction transaction = new Transaction();

        // mock
        when(transactionRepository.findByUsernameAndTransactionName(USERNAME, TRANSACTION_NAME)).thenReturn(Optional.of(transaction));

        // Action & assert
        Optional<Transaction> transactionOptional = transactionDataService.findByUsernameAndTransactionName(USERNAME, TRANSACTION_NAME);
        Assertions.assertNotNull(transactionOptional);
        Assertions.assertTrue(transactionOptional.isPresent());
        Assertions.assertEquals(transaction, transactionOptional.get());
    }

    @Test
    void findAllByUsernameAndDateTest() {
        // prepare
        final Date TODAY = new Date();
        final Integer PAGE_NUMBER = 1;
        final Integer PAGE_SIZE = 10;
        List<Transaction> transactions = List.of();

        // mock
        when(mongoOperations.find(any(), eq(Transaction.class))).thenReturn(transactions);

        // Action & assert
        List<Transaction> response = transactionDataService.findAllByUsernameAndDate(USERNAME, TODAY, PAGE_NUMBER, PAGE_SIZE);
        Assertions.assertNotNull(response);
        Assertions.assertIterableEquals(transactions, response);
    }

    @Test
    void findAllByUsernameTest() {
        // prepare
        List<Transaction> transactions = List.of();

        // mock
        when(transactionRepository.findAllByUsername(USERNAME)).thenReturn(transactions);

        // Action & assert
        List<Transaction> response = transactionDataService.findAllByUsername(USERNAME);
        Assertions.assertNotNull(response);
        Assertions.assertIterableEquals(transactions, response);
    }

    @Test
    void deleteByTransactionIdTest() {
        // mock
        doNothing().when(transactionRepository).deleteById(TRANSACTION_ID);

        // Action & assert
        Assertions.assertDoesNotThrow(() -> transactionDataService.deleteByTransactionId(TRANSACTION_ID));
    }
}