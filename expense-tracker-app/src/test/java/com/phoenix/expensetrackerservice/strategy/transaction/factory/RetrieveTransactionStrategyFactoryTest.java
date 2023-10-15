package com.phoenix.expensetrackerservice.strategy.transaction.factory;

import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.transaction.RetrieveTransactionStrategy;
import com.phoenix.expensetrackerservice.strategy.transaction.impl.RetrieveAllTransactionsStrategy;
import com.phoenix.expensetrackerservice.strategy.transaction.impl.RetrieveTransactionByPageStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RetrieveTransactionStrategyFactoryTest {

    RetrieveTransactionStrategyFactory retrieveTransactionStrategyFactory;

    @BeforeEach
    public void setup() {
        RetrieveAllTransactionsStrategy mockRetrieveAllTransactionsStrategy = mock(RetrieveAllTransactionsStrategy.class);
        RetrieveTransactionByPageStrategy mockRetrieveTransactionByPageStrategy = mock(RetrieveTransactionByPageStrategy.class);
        List<RetrieveTransactionStrategy> retrieveTransactionStrategies = List.of(
                mockRetrieveAllTransactionsStrategy,
                mockRetrieveTransactionByPageStrategy);

        when(mockRetrieveAllTransactionsStrategy.retrieveType()).thenReturn(RetrieveType.FETCH_ALL);
        when(mockRetrieveTransactionByPageStrategy.retrieveType()).thenReturn(RetrieveType.FETCH_BY_PAGE);
        retrieveTransactionStrategyFactory = spy(new RetrieveTransactionStrategyFactory(retrieveTransactionStrategies));
    }

    @ParameterizedTest
    @EnumSource(value = RetrieveType.class, names = {"FETCH_ALL", "FETCH_BY_PAGE"})
    public void testGetStrategySuccess(RetrieveType retrieveType) {
        RetrieveTransactionStrategy retrieveTransactionStrategy = assertDoesNotThrow(() -> retrieveTransactionStrategyFactory.getStrategy(retrieveType));
        assertNotNull(retrieveTransactionStrategy);
    }

    @ParameterizedTest
    @EnumSource(value = RetrieveType.class, names = {"FETCH_SINGLE_ENTITY"})
    @NullSource
    public void testGetSuccessThrowsExceptionTest(RetrieveType retrieveType) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> retrieveTransactionStrategyFactory.getStrategy(retrieveType));
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(String.format("Strategy %s not supported.", retrieveType), exception.getMessage());
    }
}