package com.phoenix.expensetrackerservice.strategy.transaction;

import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;
import com.phoenix.expensetrackerservice.utils.AuthUtils;

import java.util.List;
import java.util.Objects;

public abstract class RetrieveTransactionStrategy {
    public List<TransactionDTO> retrieve(RetrieveTransactionDTO retrieveTransactionDTO) {
        String username = AuthUtils.getUsername();
        // check if username is null
        if (Objects.isNull(username)) {
            throw new ExpenseTrackerException(ErrorConstants.USERNAME_NULL_MESSAGE, ExpenseError.SERVER_ERROR);
        }

        return retrieve(retrieveTransactionDTO, username);
    }

    public abstract List<TransactionDTO> retrieve(RetrieveTransactionDTO retrieveTransactionDTO, String username);

    public abstract RetrieveType retrieveType();
}
