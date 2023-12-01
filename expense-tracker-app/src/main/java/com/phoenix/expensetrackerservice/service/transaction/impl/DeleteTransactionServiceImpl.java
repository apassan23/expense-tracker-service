package com.phoenix.expensetrackerservice.service.transaction.impl;

import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.service.transaction.DeleteTransactionService;
import com.phoenix.expensetrackerservice.utils.AuthUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class DeleteTransactionServiceImpl implements DeleteTransactionService {
    private final TransactionDataService transactionDataService;

    public DeleteTransactionServiceImpl(TransactionDataService transactionDataService) {
        this.transactionDataService = transactionDataService;
    }

    @Override
    public TransactionDTO given(TransactionDTO transactionDTO) {
        String username = AuthUtils.getUsername();
        if (Objects.isNull(username)) {
            throw new ExpenseTrackerException("Username is null!", ExpenseError.SERVER_ERROR);
        }

        String transactionId = transactionDTO.getTransactionId();
        handleTransactionDoesNotExists(transactionId, username);

        // delete the transaction
        transactionDataService.deleteByTransactionIdAndUsername(transactionId, username);

        return transactionDTO;
    }

    private void handleTransactionDoesNotExists(String transactionId, String username) {

        AtomicBoolean transactionExists = new AtomicBoolean(transactionDataService.existsByTransactionIdAndUsername(transactionId, username));

        // throw an error if transaction does not exist
        if (!transactionExists.get()) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.TRANSACTION_NOT_PRESENT.getDescription(), ExpenseError.TRANSACTION_NOT_PRESENT);
        }
    }
}
