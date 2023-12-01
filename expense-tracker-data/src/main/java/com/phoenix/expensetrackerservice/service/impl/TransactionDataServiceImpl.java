package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.repository.TransactionRepository;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.utils.DateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionDataServiceImpl implements TransactionDataService {

    private static final String USERNAME = "username";
    private static final String TRANSACTION_DATE = "transactionDate";
    private final TransactionRepository transactionRepository;
    private final MongoOperations mongoOperations;

    public TransactionDataServiceImpl(TransactionRepository transactionRepository, MongoOperations mongoOperations) {
        this.transactionRepository = transactionRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public boolean existsByTransactionIdAndUsername(String transactionId, String username) {
        return transactionRepository.existsByTransactionIdAndUsername(transactionId, username);
    }

    @Override
    public Optional<Transaction> findByTransactionIdAndUsername(String transactionId, String username) {
        return transactionRepository.findByTransactionIdAndUsername(transactionId, username);
    }

    @Override
    public Optional<Transaction> findByUsernameAndTransactionName(String username, String transactionName) {
        return transactionRepository.findByUsernameAndTransactionName(username, transactionName);
    }

    @Override
    public List<Transaction> findAllByUsernameAndDate(String username, Date date, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Criteria criteria = buildCriteria(username, date);

        Query query = new Query();
        query.addCriteria(criteria);
        query.with(pageRequest);

        return mongoOperations.find(query, Transaction.class);
    }

    @Override
    public List<Transaction> findAllByUsername(String username) {
        return transactionRepository.findAllByUsername(username);
    }

    @Override
    public void deleteByTransactionIdAndUsername(String transactionId, String username) {
        transactionRepository.deleteById(transactionId);
    }

    private Criteria buildCriteria(String username, Date date) {
        Date nextDay = DateUtils.nextDay(date);

        List<Criteria> criterion = new ArrayList<>();
        criterion.add(Criteria.where(USERNAME).is(username));
        criterion.add(Criteria.where(TRANSACTION_DATE).gte(date).lte(nextDay));

        return new Criteria().andOperator(criterion);
    }
}
