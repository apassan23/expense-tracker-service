package com.phoenix.expensetrackerservice.service.transaction;

import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.utils.AuthUtils;

import java.util.Objects;

public abstract class TransactionService {

    public TransactionDTO given(TransactionDTO transactionDTO) {

        // check if transaction is present
        String username = AuthUtils.getUsername();

        if (Objects.isNull(username)) {
            throw new ExpenseTrackerException(ErrorConstants.USERNAME_NULL_MESSAGE, ExpenseError.SERVER_ERROR);
        }

        return given(transactionDTO, username);
    }

    public abstract TransactionDTO given(TransactionDTO transactionDTO, String username);
}
