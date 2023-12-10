package com.phoenix.expensetrackerservice.service.transaction.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.service.category.CategoryManagementService;
import com.phoenix.expensetrackerservice.service.transaction.TransactionService;
import com.phoenix.expensetrackerservice.transform.TransactionEntityBuilder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CreateTransactionServiceImpl extends TransactionService {
    private final TransactionDataService transactionDataService;
    private final CategoryManagementService categoryManagementService;

    public CreateTransactionServiceImpl(TransactionDataService transactionDataService, CategoryManagementService categoryManagementService) {
        this.transactionDataService = transactionDataService;
        this.categoryManagementService = categoryManagementService;
    }

    @Override
    public TransactionDTO given(TransactionDTO transactionDTO, String username) {

        Transaction transaction = TransactionEntityBuilder.build(username, transactionDTO);
        String transactionName = transaction.getTransactionName();
        Optional<Transaction> transactionOptional = transactionDataService.findByUsernameAndTransactionName(username, transactionName);

        // check if transaction already exists
        if (transactionOptional.isPresent()) {
            throw new ExpenseTrackerBadRequestException(ExpenseError.TRANSACTION_CREATE_ALREADY_EXISTS.getDescription(), ExpenseError.TRANSACTION_CREATE_ALREADY_EXISTS);
        }

        // check if category exists or not -> if not throw error
        String categoryId = transaction.getCategoryId();
        checkIfCategoryExists(categoryId);

        return TransactionEntityBuilder.buildFromTransaction(transactionDataService.save(transaction));
    }

    private void checkIfCategoryExists(String categoryId) {
        CategoryDTO category = categoryManagementService.retrieveCategory(categoryId);
        if (Objects.isNull(category)) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), ExpenseError.CATEGORY_DOES_NOT_EXISTS);
        }
    }
}
