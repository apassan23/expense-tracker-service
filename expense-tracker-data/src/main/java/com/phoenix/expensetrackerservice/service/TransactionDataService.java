package com.phoenix.expensetrackerservice.service;

import com.phoenix.expensetrackerservice.entity.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TransactionDataService {
    Transaction save(Transaction transaction);

    boolean existsByTransactionIdAndUsername(String transactionId, String username);

    Optional<Transaction> findByTransactionIdAndUsername(String username, String transactionId);

    Optional<Transaction> findByUsernameAndTransactionName(String username, String transactionName);

    List<Transaction> findAllByUsernameAndDate(String username, Date date, Integer pageNumber, Integer pageSize);

    List<Transaction> findAllByUsername(String username);

    void deleteByTransactionId(String transactionId);
}
