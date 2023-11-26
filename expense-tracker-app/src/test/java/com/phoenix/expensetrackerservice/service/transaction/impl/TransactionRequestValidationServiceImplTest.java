package com.phoenix.expensetrackerservice.service.transaction.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.expensetrackerservice.enums.Account;
import com.phoenix.expensetrackerservice.enums.TransactionType;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class TransactionRequestValidationServiceImplTest {

    private static final String TRANSACTION_ID = "transactionId";

    private TransactionRequestValidationServiceImpl transactionRequestValidationService;

    @BeforeEach
    void setup() {
        transactionRequestValidationService = spy(new TransactionRequestValidationServiceImpl());
    }

    @ParameterizedTest
    @NullSource
    @MethodSource(value = "transactionSourceForCreate")
    public void validateForCreateThrowsErrorTest(TransactionDTO transactionDTO) {
        // Action & assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> transactionRequestValidationService.validateForCreate(transactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, exception.getExpenseError());
    }

    @Test
    public void validateForCreateHappyPathTest() {
        // prepare
        TransactionDTO transactionDTO = generateValidTransactionDTO();
        transactionDTO.setTransactionId(null);

        // Action & assert
        Assertions.assertDoesNotThrow(() -> transactionRequestValidationService.validateForCreate(transactionDTO));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource(value = "transactionSourceForChange")
    public void validateForChangeTest(TransactionDTO transactionDTO) {
        // Action & assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> transactionRequestValidationService.validateForChange(transactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, exception.getExpenseError());
    }

    @Test
    public void validateForChangeHappyPathTest() {
        // prepare
        TransactionDTO transactionDTO = generateValidTransactionDTO();

        // Action & assert
        Assertions.assertDoesNotThrow(() -> transactionRequestValidationService.validateForChange(transactionDTO));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource(value = "transactionSourceForRetrieveAndDelete")
    public void validateForRetrieveTest(TransactionDTO transactionDTO) {
        // Action & assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> transactionRequestValidationService.validateForRetrieve(transactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, exception.getExpenseError());
    }

    @Test
    public void validateForRetrieveHappyPathTest() {
        // prepare
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(TRANSACTION_ID);

        // Action & assert
        Assertions.assertDoesNotThrow(() -> transactionRequestValidationService.validateForRetrieve(transactionDTO));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource(value = "transactionSourceForRetrieve")
    public void validateForRetrieveTest(RetrieveTransactionDTO retrieveTransactionDTO) {
        // Action & assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> transactionRequestValidationService.validateForRetrieve(retrieveTransactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, exception.getExpenseError());
    }

    @ParameterizedTest
    @MethodSource(value = "transactionSourceForRetrieveHappyPath")
    public void validateForRetrieveHappyPathTest2(RetrieveTransactionDTO retrieveTransactionDTO) {
        // Action & assert
        Assertions.assertDoesNotThrow(() -> transactionRequestValidationService.validateForRetrieve(retrieveTransactionDTO));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource(value = "transactionSourceForRetrieveAndDelete")
    public void validateForDeleteTest(TransactionDTO transactionDTO) {
        // Action & assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> transactionRequestValidationService.validateForDelete(transactionDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, exception.getExpenseError());
    }

    @Test
    public void validateForDeleteHappyPathTest() {
        // prepare
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(TRANSACTION_ID);

        // Action & assert
        Assertions.assertDoesNotThrow(() -> transactionRequestValidationService.validateForDelete(transactionDTO));
    }

    private static Stream<Arguments> transactionSourceForRetrieveHappyPath() throws IOException {
        List<RetrieveTransactionDTO> transactions = getTransactionsFromFile("src/test/resources/data/RetrieveTransactionsHappyPath.json", new TypeReference<>() {
        });

        return transactions.stream()
                .map(Arguments::of);
    }

    private static Stream<Arguments> transactionSourceForRetrieve() throws IOException {
        List<RetrieveTransactionDTO> transactions = getTransactionsFromFile("src/test/resources/data/RetrieveTransactions.json", new TypeReference<>() {
        });

        return transactions.stream()
                .map(Arguments::of);
    }

    private static Stream<Arguments> transactionSourceForRetrieveAndDelete() throws IOException {
        List<TransactionDTO> transactions = getTransactionsFromFile("src/test/resources/data/Transactions.json", new TypeReference<>() {
        });

        return transactions.stream()
                .filter(transaction -> Objects.isNull(transaction.getTransactionId()))
                .map(Arguments::of);
    }

    private static Stream<Arguments> transactionSourceForChange() throws IOException {
        List<TransactionDTO> transactions = getTransactionsFromFile("src/test/resources/data/Transactions.json", new TypeReference<>() {
        });

        return transactions.stream()
                .map(Arguments::of);
    }

    private static Stream<Arguments> transactionSourceForCreate() throws IOException {

        List<TransactionDTO> transactions = getTransactionsFromFile("src/test/resources/data/Transactions.json", new TypeReference<>() {
        });

        return transactions.stream()
                .filter(transaction -> Objects.nonNull(transaction.getTransactionId()))
                .map(Arguments::of);
    }

    private static <T> T getTransactionsFromFile(String filePath, TypeReference<T> typeReference) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String transactionString = FileUtils.readFileToString(new File(filePath));

        return objectMapper
                .readValue(transactionString, typeReference);
    }

    private TransactionDTO generateValidTransactionDTO() {
        return TransactionDTO.builder()
                .transactionId(TRANSACTION_ID)
                .transactionDate(new Date())
                .transactionMonth(Month.JANUARY.name())
                .transactionName("transactionName")
                .notes("notes")
                .transactionType(TransactionType.EXPENSE)
                .categoryId("categoryId")
                .amount(new BigDecimal(1_000_000))
                .account(Account.SAVINGS_ACCOUNT)
                .build();
    }

}