package com.phoenix.expensetrackerservice.service.transaction.impl;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.transaction.RetrieveTransactionStrategy;
import com.phoenix.expensetrackerservice.strategy.transaction.factory.RetrieveTransactionStrategyFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveTransactionServiceImplTest {

    private static final String TRANSACTION_ID = "transactionId";

    @Mock
    private RetrieveTransactionStrategyFactory retrieveTransactionStrategyFactory;

    private RetrieveTransactionServiceImpl retrieveTransactionService;

    @BeforeEach
    public void setup() {
        retrieveTransactionService = spy(new RetrieveTransactionServiceImpl(retrieveTransactionStrategyFactory));
    }

    @ParameterizedTest
    @EnumSource(value = RetrieveType.class, names = {"FETCH_SINGLE_ENTITY", "FETCH_ALL", "FETCH_BY_PAGE"})
    void givenShouldFetchSingleEntityTest(RetrieveType retrieveType) {

        // prepare
        RetrieveTransactionDTO retrieveTransactionDTO = new RetrieveTransactionDTO();

        int transactionSize = 5;

        if (retrieveType == RetrieveType.FETCH_SINGLE_ENTITY) {
            retrieveTransactionDTO.setTransactionId(TRANSACTION_ID);
            transactionSize = 1;
        } else if (retrieveType == RetrieveType.FETCH_BY_PAGE) {
            retrieveTransactionDTO.setFetchAll(Boolean.FALSE);
        }

        List<TransactionDTO> transactionDTOS = generateTransactions(transactionSize);

        RetrieveTransactionStrategy retrieveTransactionStrategy = mock(RetrieveTransactionStrategy.class);

        // mock
        when(retrieveTransactionStrategyFactory.getStrategy(retrieveType)).thenReturn(retrieveTransactionStrategy);
        when(retrieveTransactionStrategy.retrieve(retrieveTransactionDTO)).thenReturn(transactionDTOS);

        // Action & Assert
        List<TransactionDTO> actualTransactions = retrieveTransactionService.given(retrieveTransactionDTO);
        Assertions.assertNotNull(actualTransactions);
        Assertions.assertEquals(transactionSize, actualTransactions.size());

        verify(retrieveTransactionStrategyFactory, times(1)).getStrategy(retrieveType);
    }

    private List<TransactionDTO> generateTransactions(final int size) {
        return IntStream.range(0, size)
                .mapToObj(ignore -> mock(TransactionDTO.class))
                .toList();
    }
}