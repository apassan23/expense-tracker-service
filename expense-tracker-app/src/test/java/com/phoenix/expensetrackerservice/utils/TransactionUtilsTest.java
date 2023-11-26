package com.phoenix.expensetrackerservice.utils;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.enums.Account;
import com.phoenix.expensetrackerservice.enums.TransactionType;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class TransactionUtilsTest {

    private static final Map<Integer, Transaction> TRANSACTIONS_MAP;

    static {
        TRANSACTIONS_MAP = buildTransactions(5);
    }

    @ParameterizedTest
    @MethodSource("transactionSource")
    void getTransactionsTest(Integer transactionId, TransactionDTO transaction) {
        Transaction transactionFromMap = TRANSACTIONS_MAP.get(transactionId);

        Assertions.assertEquals(transactionFromMap.getTransactionId(), transaction.getTransactionId());
        Assertions.assertEquals(transactionFromMap.getTransactionDate(), transaction.getTransactionDate());
        Assertions.assertEquals(transactionFromMap.getTransactionName(), transaction.getTransactionName());
        Assertions.assertEquals(transactionFromMap.getAmount(), transaction.getAmount());
        Assertions.assertEquals(transactionFromMap.getCategoryId(), transaction.getCategoryId());
        Assertions.assertEquals(transactionFromMap.getTransactionMonth(), transaction.getTransactionMonth());
        Assertions.assertEquals(transactionFromMap.getTransactionType(), transaction.getTransactionType());
        Assertions.assertEquals(transactionFromMap.getNotes(), transaction.getNotes());
    }

    private static Stream<Arguments> transactionSource() {
        AtomicInteger counter = new AtomicInteger(0);
        return TransactionUtils.getTransactions(TRANSACTIONS_MAP.values().stream().toList())
                .stream()
                .map(transaction -> Arguments.of(counter.incrementAndGet(), transaction));
    }

    private static Map<Integer, Transaction> buildTransactions(int length) {
        final String transactionId = "dummyTransactionId%s";
        final String username = "dummyUsername%s";
        final String transactionName = "dummyTransactionName%s";
        final BigDecimal amount = new BigDecimal(1000);
        final String categoryId = "dummyCategoryId%s";
        final Date transactionDate = new Date();
        final Account account = Account.SAVINGS_ACCOUNT;
        final TransactionType transactionType = TransactionType.EXPENSE;
        final String notes = "dummyNotes%s";


        Map<Integer, Transaction> transactionMap = new HashMap<>();

        IntStream
                .range(1, length + 1)
                .forEach(idx -> {
                            Transaction transaction = new Transaction(
                                    transactionId.formatted(idx),
                                    username.formatted(idx),
                                    transactionName.formatted(idx),
                                    amount.multiply(new BigDecimal(idx)),
                                    categoryId.formatted(idx),
                                    Date.from(transactionDate.toInstant().plus(idx, ChronoUnit.DAYS)),
                                    Month.of(idx % 12 == 0 ? 12 : idx % 12).name(),
                                    account,
                                    transactionType,
                                    notes.formatted(idx)
                            );
                            transactionMap.put(idx, transaction);
                        }

                );
        return transactionMap;
    }
}