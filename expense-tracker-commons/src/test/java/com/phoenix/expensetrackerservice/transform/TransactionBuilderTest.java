package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.model.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class TransactionBuilderTest {

    @Test
    public void buildFromTransactionId() {
        String transactionId = "dummy";

        TransactionDTO transactionDTO = TransactionBuilder.buildFromTransactionId(transactionId);
        assertNotNull(transactionDTO);
        assertEquals(transactionId, transactionDTO.getTransactionId());
        assertNull(transactionDTO.getTransactionDate());
        assertNull(transactionDTO.getTransactionMonth());
        assertNull(transactionDTO.getTransactionType());
        assertNull(transactionDTO.getTransactionName());
        assertNull(transactionDTO.getAmount());
        assertNull(transactionDTO.getAccount());
        assertNull(transactionDTO.getNotes());
    }
}