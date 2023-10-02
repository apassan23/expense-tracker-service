package com.phoenix.expensetrackerservice.repository;

import com.phoenix.expensetrackerservice.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Optional<Transaction> findByTransactionName(String transactionName);
}
