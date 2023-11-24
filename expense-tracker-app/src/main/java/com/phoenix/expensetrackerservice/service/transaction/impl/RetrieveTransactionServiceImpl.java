package com.phoenix.expensetrackerservice.service.transaction.impl;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.transaction.RetrieveTransactionService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.transaction.RetrieveTransactionStrategy;
import com.phoenix.expensetrackerservice.strategy.transaction.factory.RetrieveTransactionStrategyFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RetrieveTransactionServiceImpl implements RetrieveTransactionService {
    private final RetrieveTransactionStrategyFactory retrieveTransactionStrategyFactory;

    public RetrieveTransactionServiceImpl(RetrieveTransactionStrategyFactory retrieveTransactionStrategyFactory) {
        this.retrieveTransactionStrategyFactory = retrieveTransactionStrategyFactory;
    }

    @Override
    public List<TransactionDTO> given(RetrieveTransactionDTO retrieveTransactionDTO) {

        RetrieveTransactionStrategy retrieveTransactionStrategy;

        if (Objects.nonNull(retrieveTransactionDTO.getTransactionId())) {
            retrieveTransactionStrategy = retrieveTransactionStrategyFactory.getStrategy(RetrieveType.FETCH_SINGLE_ENTITY);
        } else if (retrieveTransactionDTO.isFetchAll()) {
            retrieveTransactionStrategy = retrieveTransactionStrategyFactory.getStrategy(RetrieveType.FETCH_ALL);
        } else {
            retrieveTransactionStrategy = retrieveTransactionStrategyFactory.getStrategy(RetrieveType.FETCH_BY_PAGE);
        }

        return retrieveTransactionStrategy.retrieve(retrieveTransactionDTO);
    }
}
