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
        // fetch the transaction by transaction id and username
        String transactionId = transactionDTO.getTransactionId();
        if (!transactionDataService.existsByTransactionIdAndUsername(transactionId, username)) {
            // throw an error if transaction does not exist
            throw new ExpenseTrackerNotFoundException(ExpenseError.TRANSACTION_NOT_PRESENT.getDescription(), ExpenseError.TRANSACTION_NOT_PRESENT);
        }
        // delete the transaction
        transactionDataService.deleteByTransactionId(transactionId);
        return transactionDTO;
    }
}
