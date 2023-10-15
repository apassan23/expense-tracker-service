package com.phoenix.expensetrackerservice.strategy.category.factory;

import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.category.RetrieveCategoryStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RetrieveCategoryStrategyFactory {
    private final Map<RetrieveType, RetrieveCategoryStrategy> retrieveCategoryStrategyMap;

    public RetrieveCategoryStrategyFactory(List<RetrieveCategoryStrategy> categoryStrategies) {
        retrieveCategoryStrategyMap = categoryStrategies.stream().collect(Collectors.toMap(RetrieveCategoryStrategy::retrieveType, Function.identity()));
    }

    public RetrieveCategoryStrategy getStrategy(RetrieveType retrieveType) {
        RetrieveCategoryStrategy retrieveCategoryStrategy = retrieveCategoryStrategyMap.get(retrieveType);
        if (Objects.isNull(retrieveCategoryStrategy)) {
            throw new IllegalArgumentException(String.format("Strategy %s not supported.", retrieveType));
        }
        return retrieveCategoryStrategy;
    }
}
