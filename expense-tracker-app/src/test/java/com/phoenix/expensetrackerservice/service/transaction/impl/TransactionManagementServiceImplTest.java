package com.phoenix.expensetrackerservice.service.transaction.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.transaction.ChangeTransactionService;
import com.phoenix.expensetrackerservice.service.transaction.CreateTransactionService;
import com.phoenix.expensetrackerservice.service.transaction.DeleteTransactionService;
import com.phoenix.expensetrackerservice.service.transaction.RetrieveTransactionService;
import com.phoenix.expensetrackerservice.service.transaction.TransactionRequestValidationService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionManagementServiceImplTest {
    private static final String TRANSACTION_ID = "transactionId";
    private static final int PAGE_NUMBER = 1;
    private static final int PAGE_SIZE = 10;
    private static final List<TransactionDTO> TRANSACTIONS;

    @Mock
    private CreateTransactionService createTransactionService;
    @Mock
    private ChangeTransactionService changeTransactionService;
    @Mock
    private DeleteTransactionService deleteTransactionService;
    @Mock
    private RetrieveTransactionService retrieveTransactionService;
    @Mock
    private TransactionRequestValidationService transactionRequestValidationService;

    private TransactionManagementServiceImpl transactionManagementService;

    static {
        try {
            TRANSACTIONS = generateTransactionResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        transactionManagementService = spy(
                new TransactionManagementServiceImpl(createTransactionService, changeTransactionService,
                        deleteTransactionService, retrieveTransactionService, transactionRequestValidationService));
    }

    @Test
    void createTransactionTest() {
        // prepare
        TransactionDTO transactionDTO = new TransactionDTO();
        TransactionDTO response = TRANSACTIONS.stream().findAny().orElseThrow();

        // mock
        doNothing().when(transactionRequestValidationService).validateForCreate(transactionDTO);
        when(createTransactionService.given(transactionDTO)).thenReturn(response);

        // Action & assert
        TransactionDTO actualResponse = transactionManagementService.createTransaction(transactionDTO);
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(response.getTransactionId(), actualResponse.getTransactionId());
        Assertions.assertEquals(response.getTransactionName(), actualResponse.getTransactionName());
        Assertions.assertEquals(response.getTransactionType(), actualResponse.getTransactionType());
        Assertions.assertEquals(response.getTransactionMonth(), actualResponse.getTransactionMonth());
        Assertions.assertEquals(response.getCategoryId(), actualResponse.getCategoryId());
        Assertions.assertEquals(response.getAmount(), actualResponse.getAmount());
        Assertions.assertEquals(response.getTransactionDate(), actualResponse.getTransactionDate());
        Assertions.assertEquals(response.getNotes(), actualResponse.getNotes());
        Assertions.assertEquals(response.getAccount(), actualResponse.getAccount());
    }

    @Test
    void createTransactionThrowsErrorTest() {
        // prepare
        TransactionDTO transactionDTO = new TransactionDTO();

        // mock
        doThrow(ExpenseTrackerException.class).when(transactionRequestValidationService)
                .validateForCreate(transactionDTO);

        // Action & assert
        Assertions.assertThrows(ExpenseTrackerException.class,
                () -> transactionManagementService.createTransaction(transactionDTO));

        verify(createTransactionService, times(0)).given(transactionDTO);
    }

    @Test
    void retrieveTransactionTest() {
        // prepare
        TransactionDTO response = TRANSACTIONS.stream().findFirst().orElseThrow();

        // mock
        doNothing().when(transactionRequestValidationService).validateForRetrieve(any(RetrieveTransactionDTO.class));
        when(retrieveTransactionService.given(any(RetrieveTransactionDTO.class))).thenReturn(TRANSACTIONS);

        // Action & assert
        TransactionDTO actualResponse = transactionManagementService.retrieveTransaction(TRANSACTION_ID);
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(response.getTransactionId(), actualResponse.getTransactionId());
        Assertions.assertEquals(response.getTransactionName(), actualResponse.getTransactionName());
        Assertions.assertEquals(response.getTransactionType(), actualResponse.getTransactionType());
        Assertions.assertEquals(response.getTransactionMonth(), actualResponse.getTransactionMonth());
        Assertions.assertEquals(response.getCategoryId(), actualResponse.getCategoryId());
        Assertions.assertEquals(response.getAmount(), actualResponse.getAmount());
        Assertions.assertEquals(response.getTransactionDate(), actualResponse.getTransactionDate());
        Assertions.assertEquals(response.getNotes(), actualResponse.getNotes());
        Assertions.assertEquals(response.getAccount(), actualResponse.getAccount());
    }

    @Test
    void retrieveTransactionThrowsErrorTest() {
        // mock
        doThrow(ExpenseTrackerException.class).when(transactionRequestValidationService)
                .validateForRetrieve(any(RetrieveTransactionDTO.class));

        // Action & assert
        Assertions.assertThrows(ExpenseTrackerException.class,
                () -> transactionManagementService.retrieveTransaction(TRANSACTION_ID));

        verify(retrieveTransactionService, times(0)).given(any(RetrieveTransactionDTO.class));
    }

    @Test
    void retrieveTransactionsFetchByPageTest() {
        // prepare
        RetrieveTransactionDTO retrieveTransactionDTO = new RetrieveTransactionDTO();
        retrieveTransactionDTO.setPageNumber(PAGE_NUMBER);
        retrieveTransactionDTO.setPageSize(PAGE_SIZE);

        // mock
        doNothing().when(transactionRequestValidationService).validateForRetrieve(retrieveTransactionDTO);
        when(retrieveTransactionService.given(retrieveTransactionDTO)).thenReturn(TRANSACTIONS);

        // Action & assert
        List<TransactionDTO> transactions = transactionManagementService.retrieveTransactions(retrieveTransactionDTO);
        Assertions.assertNotNull(transactions);
        Assertions.assertFalse(retrieveTransactionDTO.isFetchAll());
        Assertions.assertIterableEquals(TRANSACTIONS, transactions);
    }

    @Test
    void retrieveTransactionsFetchAllTest() {
        // prepare
        RetrieveTransactionDTO retrieveTransactionDTO = new RetrieveTransactionDTO();

        // mock
        doNothing().when(transactionRequestValidationService).validateForRetrieve(retrieveTransactionDTO);
        when(retrieveTransactionService.given(retrieveTransactionDTO)).thenReturn(TRANSACTIONS);

        // Action & assert
        List<TransactionDTO> transactions = transactionManagementService.retrieveTransactions(retrieveTransactionDTO);
        Assertions.assertNotNull(transactions);
        Assertions.assertTrue(retrieveTransactionDTO.isFetchAll());
        Assertions.assertIterableEquals(TRANSACTIONS, transactions);
    }

    @Test
    void changeTransactionTest() {
        // prepare
        TransactionDTO transactionDTO = TRANSACTIONS.stream().findFirst().orElseThrow();
        TransactionDTO response = clone(transactionDTO);
        response.setAmount(new BigDecimal(3000));

        // mock
        doNothing().when(transactionRequestValidationService).validateForChange(transactionDTO);
        when(changeTransactionService.given(transactionDTO)).thenReturn(response);

        // Action & assert
        TransactionDTO actualResponse = transactionManagementService.changeTransaction(transactionDTO);
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(response.getTransactionId(), actualResponse.getTransactionId());
        Assertions.assertEquals(response.getTransactionName(), actualResponse.getTransactionName());
        Assertions.assertEquals(response.getTransactionType(), actualResponse.getTransactionType());
        Assertions.assertEquals(response.getTransactionMonth(), actualResponse.getTransactionMonth());
        Assertions.assertEquals(response.getCategoryId(), actualResponse.getCategoryId());
        Assertions.assertEquals(response.getAmount(), actualResponse.getAmount());
        Assertions.assertNotEquals(response.getAmount(), transactionDTO.getAmount());
        Assertions.assertEquals(response.getTransactionDate(), actualResponse.getTransactionDate());
        Assertions.assertEquals(response.getNotes(), actualResponse.getNotes());
        Assertions.assertEquals(response.getAccount(), actualResponse.getAccount());
    }

    @Test
    void changeTransactionThrowsErrorTest() {
        // prepare
        TransactionDTO transactionDTO = TRANSACTIONS.stream().findFirst().orElseThrow();

        // mock
        doThrow(ExpenseTrackerException.class).when(transactionRequestValidationService)
                .validateForChange(transactionDTO);

        // Action & assert
        Assertions.assertThrows(ExpenseTrackerException.class,
                () -> transactionManagementService.changeTransaction(transactionDTO));

        verify(changeTransactionService, times(0)).given(transactionDTO);
    }

    @Test
    void deleteTransactionTest() {
        // mock
        doNothing().when(transactionRequestValidationService).validateForDelete(any(TransactionDTO.class));
        when(deleteTransactionService.given(any(TransactionDTO.class))).thenReturn(new TransactionDTO());

        // Action & assert
        Assertions.assertDoesNotThrow(() -> transactionManagementService.deleteTransaction(TRANSACTION_ID));
    }

    @Test
    void deleteTransactionThrowsErrorTest() {
        // mock
        doThrow(ExpenseTrackerException.class).when(transactionRequestValidationService)
                .validateForDelete(any(TransactionDTO.class));

        // Action & assert
        Assertions.assertThrows(ExpenseTrackerException.class,
                () -> transactionManagementService.deleteTransaction(TRANSACTION_ID));

        verify(deleteTransactionService, times(0)).given(any(TransactionDTO.class));
    }

    private static List<TransactionDTO> generateTransactionResponse() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        String json = FileUtils.readFileToString(new File("src/test/resources/data/Transactions.json"), "UTF-8");

        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

    private static TransactionDTO clone(TransactionDTO transactionDTO) {
        TransactionDTO newTransaction = new TransactionDTO();

        newTransaction.setTransactionId(transactionDTO.getTransactionId());
        newTransaction.setTransactionName(transactionDTO.getTransactionName());
        newTransaction.setTransactionDate(transactionDTO.getTransactionDate());
        newTransaction.setTransactionMonth(transactionDTO.getTransactionMonth());
        newTransaction.setAccount(transactionDTO.getAccount());
        newTransaction.setTransactionType(transactionDTO.getTransactionType());
        newTransaction.setAmount(new BigDecimal(transactionDTO.getAmount().intValue()));
        newTransaction.setCategoryId(transactionDTO.getCategoryId());
        newTransaction.setNotes(transactionDTO.getNotes());

        return newTransaction;
    }
}