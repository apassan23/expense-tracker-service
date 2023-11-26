package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.enums.Account;
import com.phoenix.expensetrackerservice.enums.TransactionType;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Date;

class TransactionEntityBuilderTest {

    @Test
    void buildFromTransactionDto() {
        // prepare
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId("transactionId")
                .account(Account.SAVINGS_ACCOUNT)
                .transactionName("transactionName")
                .notes("notes")
                .transactionType(TransactionType.EXPENSE)
                .categoryId("categoryId")
                .transactionMonth(Month.NOVEMBER.name())
                .transactionDate(new Date())
                .amount(new BigDecimal(1000))
                .build();

        // Action & assert
        Transaction transaction = TransactionEntityBuilder.buildFromTransactionDto(transactionDTO);
        Assertions.assertNotNull(transaction);
        Assertions.assertEquals(transactionDTO.getTransactionId(), transaction.getTransactionId());
        Assertions.assertEquals(transactionDTO.getAccount(), transaction.getAccount());
        Assertions.assertEquals(transactionDTO.getTransactionName(), transaction.getTransactionName());
        Assertions.assertEquals(transactionDTO.getNotes(), transaction.getNotes());
        Assertions.assertEquals(transactionDTO.getTransactionType(), transaction.getTransactionType());
        Assertions.assertEquals(transactionDTO.getCategoryId(), transaction.getCategoryId());
        Assertions.assertEquals(transactionDTO.getTransactionMonth(), transaction.getTransactionMonth());
        Assertions.assertEquals(transactionDTO.getTransactionDate(), transaction.getTransactionDate());
        Assertions.assertEquals(transactionDTO.getAmount(), transaction.getAmount());
    }

}