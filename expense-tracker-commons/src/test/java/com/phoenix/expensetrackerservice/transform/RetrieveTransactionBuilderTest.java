package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class RetrieveTransactionBuilderTest {

    @Test
    public void testBuild() {
        String transactionId = "dummy";
        Integer pageNumber = 1;
        Integer pageSize = 10;

        RetrieveTransactionDTO retrieveTransactionDTO = RetrieveTransactionBuilder.build(transactionId, pageNumber, pageSize, Boolean.FALSE);
        assertNotNull(retrieveTransactionDTO);
        assertEquals(transactionId, retrieveTransactionDTO.getTransactionId());
        assertEquals(pageNumber, retrieveTransactionDTO.getPageNumber());
        assertEquals(pageSize, retrieveTransactionDTO.getPageSize());
        assertEquals(Boolean.FALSE, retrieveTransactionDTO.isFetchAll());
    }

    @Test
    public void testBuild1() {
        Integer pageNumber = 1;
        Integer pageSize = 10;

        RetrieveTransactionDTO retrieveTransactionDTO = RetrieveTransactionBuilder.build(pageNumber, pageSize, Boolean.FALSE);
        assertNotNull(retrieveTransactionDTO);
        assertNull(retrieveTransactionDTO.getTransactionId());
        assertEquals(pageNumber, retrieveTransactionDTO.getPageNumber());
        assertEquals(pageSize, retrieveTransactionDTO.getPageSize());
        assertEquals(Boolean.FALSE, retrieveTransactionDTO.isFetchAll());
    }

    @Test
    public void testBuild2() {
        Integer pageNumber = 1;
        Integer pageSize = 10;

        RetrieveTransactionDTO retrieveTransactionDTO = RetrieveTransactionBuilder.build(pageNumber, pageSize);
        assertNotNull(retrieveTransactionDTO);
        assertNull(retrieveTransactionDTO.getTransactionId());
        assertEquals(pageNumber, retrieveTransactionDTO.getPageNumber());
        assertEquals(pageSize, retrieveTransactionDTO.getPageSize());
        assertEquals(Boolean.FALSE, retrieveTransactionDTO.isFetchAll());
    }

    @Test
    public void testBuild3() {
        String transactionId = "dummy";

        RetrieveTransactionDTO retrieveTransactionDTO = RetrieveTransactionBuilder.build(transactionId);
        assertNotNull(retrieveTransactionDTO);
        assertEquals(transactionId, retrieveTransactionDTO.getTransactionId());
        assertNull(retrieveTransactionDTO.getPageNumber());
        assertNull(retrieveTransactionDTO.getPageSize());
        assertEquals(Boolean.FALSE, retrieveTransactionDTO.isFetchAll());
    }
}