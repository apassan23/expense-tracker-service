package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.repository.TransactionRepository;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionDataServiceImpl implements TransactionDataService {

    private final TransactionRepository transactionRepository;

    public TransactionDataServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public boolean existsByTransactionId(String transactionId) {
        return transactionRepository.existsById(transactionId);
    }

    @Override
    public Optional<Transaction> findByTransactionId(String transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Override
    public Optional<Transaction> findByTransactionName(String transactionName) {
        return transactionRepository.findByTransactionName(transactionName);
    }

    @Override
    public List<Transaction> findAll(Integer pageNumber, Integer pageSize) {
        Page<Transaction> transactionPage = transactionRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return transactionPage.getContent();
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public void deleteByTransactionId(String transactionId) {
        transactionRepository.deleteById(transactionId);
    }
}
