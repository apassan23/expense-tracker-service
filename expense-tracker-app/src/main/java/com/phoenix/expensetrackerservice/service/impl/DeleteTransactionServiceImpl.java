package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.DeleteTransactionService;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import org.springframework.stereotype.Service;

@Service
public class DeleteTransactionServiceImpl implements DeleteTransactionService {
    private final TransactionDataService transactionDataService;

    public DeleteTransactionServiceImpl(TransactionDataService transactionDataService) {
        this.transactionDataService = transactionDataService;
    }

    @Override
    public TransactionDTO given(TransactionDTO transactionDTO) {
        // fetch the transaction by transaction id
        String transactionId = transactionDTO.getTransactionId();
        if(!transactionDataService.existsByTransactionId(transactionId)) {
        // throw an error if transaction does not exist
            throw new ExpenseTrackerNotFoundException(ExpenseError.TRANSACTION_NOT_PRESENT.getDescription(), ExpenseError.TRANSACTION_NOT_PRESENT);
        }
        // delete the transaction
        transactionDataService.deleteByTransactionId(transactionId);
        return transactionDTO;
    }
}
