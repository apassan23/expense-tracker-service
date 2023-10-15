package com.phoenix.expensetrackerservice.service.transaction;

import com.phoenix.expensetrackerservice.model.TransactionDTO;

import java.util.List;

public interface TransactionManagementService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    TransactionDTO retrieveTransaction(String transactionId);

    List<TransactionDTO> retrieveTransactions(Integer pageNumber, Integer pageSize);

    TransactionDTO changeTransaction(TransactionDTO transactionDTO);

    void deleteTransaction(String transactionId);
}
