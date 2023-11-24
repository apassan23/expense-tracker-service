package com.phoenix.expensetrackerservice.service.transaction.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.service.category.CategoryManagementService;
import com.phoenix.expensetrackerservice.service.transaction.ChangeTransactionService;
import com.phoenix.expensetrackerservice.transform.TransactionEntityBuilder;
import com.phoenix.expensetrackerservice.utils.AuthUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ChangeTransactionServiceImpl implements ChangeTransactionService {
    private final TransactionDataService transactionDataService;
    private final CategoryManagementService categoryManagementService;

    public ChangeTransactionServiceImpl(TransactionDataService transactionDataService, CategoryManagementService categoryManagementService) {
        this.transactionDataService = transactionDataService;
        this.categoryManagementService = categoryManagementService;
    }

    @Override
    public TransactionDTO given(TransactionDTO transactionDTO) {
        // check if transaction is present
        String username = AuthUtils.getUsername();

        if (Objects.isNull(username)) {
            throw new ExpenseTrackerException("Username is Null!", ExpenseError.SERVER_ERROR);
        }

        String transactionId = transactionDTO.getTransactionId();
        Optional<Transaction> transactionOptional = transactionDataService.findByTransactionIdAndUsername(transactionId, username);

        if (transactionOptional.isEmpty()) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.TRANSACTION_NOT_PRESENT.getDescription(), ExpenseError.TRANSACTION_NOT_PRESENT);
        }

        // check if category is present
        Transaction transaction = transactionOptional.get();
        transactionDTO.setTransactionDate(transaction.getTransactionDate());
        String categoryId = transactionDTO.getCategoryId();


        // check if category in request is different, then only make the retrieve call
        if (!categoryId.equals(transaction.getCategoryId())) {
            checkIfCategoryExists(categoryId);
        }

        Transaction transactionRequest = TransactionEntityBuilder.build(username, transactionDTO);
        return TransactionEntityBuilder.buildFromTransaction(transactionDataService.save(transactionRequest));
    }

    private void checkIfCategoryExists(String categoryId) {
        CategoryDTO category = categoryManagementService.retrieveCategory(categoryId);
        if (Objects.isNull(category)) {
            throw new ExpenseTrackerNotFoundException(ExpenseError.CATEGORY_DOES_NOT_EXISTS.getDescription(), ExpenseError.CATEGORY_DOES_NOT_EXISTS);
        }
    }
}
