package com.phoenix.expensetrackerservice.utils;

import com.phoenix.expensetrackerservice.enums.Account;
import com.phoenix.expensetrackerservice.enums.TransactionType;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class JsonUtilsTest {

    private static String SAMPLE_JSON = "{\"a\": \"b\", \"c\": 1}";

    @Test
    public void testGetJsonString() {
        TransactionDTO transactionDTO = getMockData();
        Assertions.assertDoesNotThrow(() -> JsonUtils.getJsonString(transactionDTO));
    }

    @Test
    void parseTest() {
        // action & assert
        Map<String, Object> parsedJson = Assertions.assertDoesNotThrow(() -> JsonUtils.parse(SAMPLE_JSON));
        Assertions.assertTrue(parsedJson.containsKey("a"));
        Assertions.assertTrue(parsedJson.containsKey("c"));
        Assertions.assertInstanceOf(String.class, parsedJson.get("a"));
        Assertions.assertInstanceOf(Integer.class, parsedJson.get("c"));
        Assertions.assertEquals("b", parsedJson.get("a"));
        Assertions.assertEquals(1, parsedJson.get("c"));
    }

    @Test
    void parseWithClassTest() {
        // action & assert
        SampleClass parsedJson = Assertions.assertDoesNotThrow(() -> JsonUtils.parse(SAMPLE_JSON, SampleClass.class));
        Assertions.assertNotNull(parsedJson);
        Assertions.assertEquals("b", parsedJson.getA());
        Assertions.assertEquals(1, parsedJson.getC());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class SampleClass {
        private String a;
        private int c;
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