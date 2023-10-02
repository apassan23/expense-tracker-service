package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.RetrieveTransactionService;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.transform.TransactionEntityBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RetrieveTransactionServiceImpl implements RetrieveTransactionService {
    private final TransactionDataService transactionDataService;

    public RetrieveTransactionServiceImpl(TransactionDataService transactionDataService) {
        this.transactionDataService = transactionDataService;
    }

    @Override
    public TransactionDTO given(TransactionDTO transactionDTO) {
        String transactionId = transactionDTO.getTransactionId();
        Optional<Transaction> transaction = transactionDataService.findByTransactionId(transactionId);
        // transaction retrieve response is empty
        if(transaction.isEmpty()) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.TRANSACTION_NOT_PRESENT.getDescription(), ExpenseError.TRANSACTION_NOT_PRESENT);
        }
        return TransactionEntityBuilder.buildFromTransaction(transaction.get());
    }

    @Override
    public List<TransactionDTO> given(RetrieveTransactionDTO retrieveTransactionDTO) {
        if(retrieveTransactionDTO.isFetchAll()) {
            List<Transaction> transactions = transactionDataService.findAll();
            return getTransactions(transactions);
        }

        Integer pageNumber = retrieveTransactionDTO.getPageNumber();
        Integer pageSize = retrieveTransactionDTO.getPageSize();
        List<Transaction> transactions = transactionDataService.findAll(pageNumber, pageSize);
        return getTransactions(transactions);
    }

    private List<TransactionDTO> getTransactions(List<Transaction> transactions) {
        return transactions.stream().map(TransactionEntityBuilder::buildFromTransaction).toList();
    }
}
