package com.phoenix.expensetrackerservice.repository;

import com.phoenix.expensetrackerservice.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Optional<Transaction> findByUsernameAndTransactionName(String username, String transactionName);

    Optional<Transaction> findByTransactionIdAndUsername(String id, String username);

    List<Transaction> findAllByUsername(String username);

}
