package com.phoenix.expensetrackerservice.service;

import com.phoenix.expensetrackerservice.entity.Transaction;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TransactionDataService {
    Transaction save(Transaction transaction);

    boolean existsByTransactionId(String transactionId);

    Optional<Transaction> findByTransactionId(String transactionId);

    Optional<Transaction> findByTransactionName(String transactionName);

    List<Transaction> findAll(Integer pageNumber, Integer pageSize);

    List<Transaction> findAll();

    void deleteByTransactionId(String transactionId);
}
