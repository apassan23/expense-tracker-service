package com.phoenix.expensetrackerservice.strategy.category.factory;

import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.category.RetrieveCategoryStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveCategoryStrategyFactoryTest {

    RetrieveCategoryStrategyFactory retrieveCategoryStrategyFactory;

    @BeforeEach
    void setup() {
        RetrieveCategoryStrategy retrieveCategoryStrategyWithFetchByPage = mock(RetrieveCategoryStrategy.class);
        RetrieveCategoryStrategy retrieveCategoryStrategyWithFetchAll = mock(RetrieveCategoryStrategy.class);

        when(retrieveCategoryStrategyWithFetchByPage.retrieveType()).thenReturn(RetrieveType.FETCH_BY_PAGE);
        when(retrieveCategoryStrategyWithFetchAll.retrieveType()).thenReturn(RetrieveType.FETCH_ALL);

        retrieveCategoryStrategyFactory = spy(new RetrieveCategoryStrategyFactory(List.of(retrieveCategoryStrategyWithFetchByPage, retrieveCategoryStrategyWithFetchAll)));
    }

    @ParameterizedTest
    @EnumSource(value = RetrieveType.class, names = {"FETCH_BY_PAGE", "FETCH_ALL"})
    void getStrategyTest(RetrieveType retrieveType) {
        RetrieveCategoryStrategy retrieveCategoryStrategy = retrieveCategoryStrategyFactory.getStrategy(retrieveType);

        Assertions.assertNotNull(retrieveCategoryStrategy);
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(value = RetrieveType.class, names = {"FETCH_SINGLE_ENTITY"})
    void getStrategyWithNullAndInvalidStrategyTest(RetrieveType retrieveType) {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> retrieveCategoryStrategyFactory.getStrategy(retrieveType));

        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals("Strategy %s not supported.".formatted(retrieveType), exception.getMessage());
    }
}