package com.phoenix.expensetrackerservice.strategy.transaction.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.transaction.RetrieveTransactionStrategy;
import com.phoenix.expensetrackerservice.util.TransactionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class RetrieveAllTransactionsStrategy implements RetrieveTransactionStrategy {
    private final TransactionDataService transactionDataService;

    public RetrieveAllTransactionsStrategy(TransactionDataService transactionDataService) {
        this.transactionDataService = transactionDataService;
    }

    @Override
    public List<TransactionDTO> retrieve(RetrieveTransactionDTO retrieveTransactionDTO) {
        List<Transaction> transactions = transactionDataService.findAll();
        return Optional.ofNullable(transactions)
                .map(TransactionUtils::getTransactions)
                .orElse(Collections.emptyList());
    }

    @Override
    public RetrieveType retrieveType() {
        return RetrieveType.FETCH_ALL;
    }
}
