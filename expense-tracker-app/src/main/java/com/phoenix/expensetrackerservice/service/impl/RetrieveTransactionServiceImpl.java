package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.RetrieveTransactionService;
import com.phoenix.expensetrackerservice.strategy.RetrieveTransactionStrategy;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.factory.RetrieveStrategyFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RetrieveTransactionServiceImpl implements RetrieveTransactionService {
    private final RetrieveStrategyFactory retrieveStrategyFactory;

    public RetrieveTransactionServiceImpl(RetrieveStrategyFactory retrieveStrategyFactory) {
        this.retrieveStrategyFactory = retrieveStrategyFactory;
    }

    @Override
    public List<TransactionDTO> given(RetrieveTransactionDTO retrieveTransactionDTO) {
        RetrieveTransactionStrategy retrieveTransactionStrategy;
        if (Objects.nonNull(retrieveTransactionDTO.getTransactionId())) {
            retrieveTransactionStrategy = retrieveStrategyFactory.getStrategy(RetrieveType.FETCH_SINGLE_TRANSACTION);
        } else if (retrieveTransactionDTO.isFetchAll()) {
            retrieveTransactionStrategy = retrieveStrategyFactory.getStrategy(RetrieveType.FETCH_ALL);
        } else {
            retrieveTransactionStrategy = retrieveStrategyFactory.getStrategy(RetrieveType.FETCH_BY_PAGE);
        }

        return retrieveTransactionStrategy.retrieve(retrieveTransactionDTO);
    }
}
