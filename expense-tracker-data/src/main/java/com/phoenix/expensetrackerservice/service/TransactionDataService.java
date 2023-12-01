package com.phoenix.expensetrackerservice.service;

import com.phoenix.expensetrackerservice.entity.Transaction;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TransactionDataService {
    @CachePut(cacheNames = "transactions", key = "{#transaction.transactionId, #transaction.username}", unless = "#result == null")
    Transaction save(Transaction transaction);

    boolean existsByTransactionIdAndUsername(String transactionId, String username);

    @Cacheable(cacheNames = "transactions", key = "{#transactionId, #username}", unless = "#result == null")
    Optional<Transaction> findByTransactionIdAndUsername(String transactionId, String username);

    Optional<Transaction> findByUsernameAndTransactionName(String username, String transactionName);

    List<Transaction> findAllByUsernameAndDate(String username, Date date, Integer pageNumber, Integer pageSize);

    List<Transaction> findAllByUsername(String username);

    @CacheEvict(cacheNames = "transactions", key = "{#transactionId, #username}")
    void deleteByTransactionIdAndUsername(String transactionId, String username);
}
