package com.phoenix.expensetrackerservice.strategy.factory;

import com.phoenix.expensetrackerservice.strategy.RetrieveTransactionStrategy;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RetrieveStrategyFactory {

    private final Map<RetrieveType, RetrieveTransactionStrategy> retrieveStrategyMap;

    public RetrieveStrategyFactory(List<RetrieveTransactionStrategy> retrieveStrategies) {
        retrieveStrategyMap = retrieveStrategies.stream().collect(Collectors.toMap(RetrieveTransactionStrategy::retrieveType, Function.identity()));
    }

    public RetrieveTransactionStrategy getStrategy(RetrieveType retrieveType) {
        RetrieveTransactionStrategy retrieveTransactionStrategy = retrieveStrategyMap.get(retrieveType);
        if (Objects.isNull(retrieveTransactionStrategy)) {
            throw new IllegalArgumentException(String.format("Strategy %s not supported.", retrieveType));
        }

        return retrieveTransactionStrategy;
    }
}
