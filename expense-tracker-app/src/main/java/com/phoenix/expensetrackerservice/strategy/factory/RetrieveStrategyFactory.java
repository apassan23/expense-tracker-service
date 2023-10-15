package com.phoenix.expensetrackerservice.strategy.factory;

import com.phoenix.expensetrackerservice.strategy.RetrieveStrategy;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RetrieveStrategyFactory {

    private final Map<RetrieveType, RetrieveStrategy> retrieveStrategyMap;

    public RetrieveStrategyFactory(List<RetrieveStrategy> retrieveStrategies) {
        retrieveStrategyMap = retrieveStrategies.stream().collect(Collectors.toMap(RetrieveStrategy::retrieveType, Function.identity()));
    }

    public RetrieveStrategy getStrategy(RetrieveType retrieveType) {
        RetrieveStrategy retrieveStrategy = retrieveStrategyMap.get(retrieveType);
        if (Objects.isNull(retrieveStrategy)) {
            throw new IllegalArgumentException(String.format("Strategy %s not supported.", retrieveType));
        }

        return retrieveStrategy;
    }
}
