package com.phoenix.expensetrackerservice.strategy.transaction.impl;

import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.transform.TransactionEntityBuilder;
import com.phoenix.expensetrackerservice.utils.AuthUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class RetrieveTransactionStrategy implements com.phoenix.expensetrackerservice.strategy.transaction.RetrieveTransactionStrategy {

    private final TransactionDataService transactionDataService;

    public RetrieveTransactionStrategy(TransactionDataService transactionDataService) {
        this.transactionDataService = transactionDataService;
    }

    @Override
    public List<TransactionDTO> retrieve(RetrieveTransactionDTO retrieveTransactionDTO) {
        String transactionId = retrieveTransactionDTO.getTransactionId();
        String username = AuthUtils.getUsername();
        if (Objects.isNull(username)) {
            throw new ExpenseTrackerException(ErrorConstants.USERNAME_NULL_MESSAGE, ExpenseError.SERVER_ERROR);
        }
        Optional<Transaction> transaction = transactionDataService.findByTransactionIdAndUsername(transactionId, username);
        // transaction retrieve response is empty
        if (transaction.isEmpty()) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.TRANSACTION_NOT_PRESENT.getDescription(), ExpenseError.TRANSACTION_NOT_PRESENT);
        }
        TransactionDTO transactionDTO = TransactionEntityBuilder.buildFromTransaction(transaction.get());
        return Collections.singletonList(transactionDTO);
    }

    @Override
    public RetrieveType retrieveType() {
        return RetrieveType.FETCH_SINGLE_ENTITY;
    }
}
