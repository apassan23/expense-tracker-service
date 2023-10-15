package com.phoenix.expensetrackerservice.strategy.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveTransactionStrategy;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.util.TransactionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class RetrieveTransactionByPageStrategy implements RetrieveTransactionStrategy {
    private final TransactionDataService transactionDataService;

    public RetrieveTransactionByPageStrategy(TransactionDataService transactionDataService) {
        this.transactionDataService = transactionDataService;
    }

    @Override
    public List<TransactionDTO> retrieve(RetrieveTransactionDTO retrieveTransactionDTO) {
        Integer pageNumber = retrieveTransactionDTO.getPageNumber();
        Integer pageSize = retrieveTransactionDTO.getPageSize();
        List<Transaction> transactions = transactionDataService.findAll(pageNumber, pageSize);
        return Optional.ofNullable(transactions)
                .map(TransactionUtils::getTransactions)
                .orElse(Collections.emptyList());
    }

    @Override
    public RetrieveType retrieveType() {
        return RetrieveType.FETCH_BY_PAGE;
    }
}
