package com.phoenix.expensetrackerservice.strategy.transaction.impl;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionDataService;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.strategy.transaction.RetrieveTransactionStrategy;
import com.phoenix.expensetrackerservice.utils.AuthUtils;
import com.phoenix.expensetrackerservice.utils.DateUtils;
import com.phoenix.expensetrackerservice.utils.TransactionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class RetrieveTransactionByPageStrategy implements RetrieveTransactionStrategy {
    private final TransactionDataService transactionDataService;

    public RetrieveTransactionByPageStrategy(TransactionDataService transactionDataService) {
        this.transactionDataService = transactionDataService;
    }

    @Override
    public List<TransactionDTO> retrieve(RetrieveTransactionDTO retrieveTransactionDTO) {
        String username = AuthUtils.getUsername();
        try {
            if (Objects.isNull(username)) {
                throw new ExpenseTrackerException("Username is null!", ExpenseError.SERVER_ERROR);
            }
            Date date = DateUtils.parse(retrieveTransactionDTO.getDate());
            Integer pageNumber = retrieveTransactionDTO.getPageNumber();
            Integer pageSize = retrieveTransactionDTO.getPageSize();
            List<Transaction> transactions = transactionDataService.findAllByUsernameAndDate(username, date, pageNumber, pageSize);
            return Optional.ofNullable(transactions)
                    .map(TransactionUtils::getTransactions)
                    .orElse(Collections.emptyList());
        } catch (Exception exception) {
            if (exception instanceof ExpenseTrackerException e) {
                throw new ExpenseTrackerException(String.format("Error: %s", e.getMessage()), e.getExpenseError(), e);
            } else {
                throw new ExpenseTrackerException(String.format("Error: %s", exception.getMessage()), ExpenseError.SERVER_ERROR, exception);
            }
        }
    }

    @Override
    public RetrieveType retrieveType() {
        return RetrieveType.FETCH_BY_PAGE;
    }
}
