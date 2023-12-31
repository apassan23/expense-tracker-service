package com.phoenix.expensetrackerservice.config.mongo;

import com.phoenix.expensetrackerservice.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepositoryIT extends MongoRepository<Transaction, String> {
    Page<Transaction> findAll(Pageable pageable);
}
