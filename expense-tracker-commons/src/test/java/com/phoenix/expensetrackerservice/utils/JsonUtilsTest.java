package com.phoenix.expensetrackerservice.utils;

import com.phoenix.expensetrackerservice.enums.Account;
import com.phoenix.expensetrackerservice.enums.TransactionType;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class JsonUtilsTest {

    @Test
    public void testGetJsonString() {
        TransactionDTO transactionDTO = getMockData();
        Assertions.assertDoesNotThrow(() -> JsonUtils.getJsonString(transactionDTO));
    }

    private TransactionDTO getMockData() {
        return TransactionDTO.builder()
                .categoryId("23544536")
                .notes("transaction")
                .account(Account.CHECKING_ACCOUNT)
                .amount(BigDecimal.TWO)
                .transactionDate(new Date())
                .transactionId("95796845967")
                .transactionMonth("JAN")
                .transactionName("Dummy")
                .transactionType(TransactionType.EXPENSE)
                .build();
    }
}