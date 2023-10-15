package com.phoenix.expensetrackerservice.strategy.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.transform.TransactionEntityBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class RetrieveTransactionTransactionStrategy implements com.phoenix.expensetrackerservice.strategy.RetrieveTransactionStrategy {

    private final TransactionDataService transactionDataService;

    public RetrieveTransactionTransactionStrategy(TransactionDataService transactionDataService) {
        this.transactionDataService = transactionDataService;
    }

    @Override
    public List<TransactionDTO> retrieve(RetrieveTransactionDTO retrieveTransactionDTO) {
        String transactionId = retrieveTransactionDTO.getTransactionId();
        Optional<Transaction> transaction = transactionDataService.findByTransactionId(transactionId);
        // transaction retrieve response is empty
        if (transaction.isEmpty()) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.TRANSACTION_NOT_PRESENT.getDescription(), ExpenseError.TRANSACTION_NOT_PRESENT);
        }
        TransactionDTO transactionDTO = TransactionEntityBuilder.buildFromTransaction(transaction.get());
        return Collections.singletonList(transactionDTO);
    }

    @Override
    public RetrieveType retrieveType() {
        return RetrieveType.FETCH_SINGLE_TRANSACTION;
    }
}
