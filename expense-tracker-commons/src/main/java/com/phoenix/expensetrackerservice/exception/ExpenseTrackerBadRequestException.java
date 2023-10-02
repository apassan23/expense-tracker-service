package com.phoenix.expensetrackerservice.exception;

import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;

public class ExpenseTrackerBadRequestException extends ExpenseTrackerException{
    public ExpenseTrackerBadRequestException(ExpenseError expenseError) {
        super(expenseError);
    }

    public ExpenseTrackerBadRequestException(String message, ExpenseError expenseError) {
        super(message, expenseError);
    }

    public ExpenseTrackerBadRequestException(String message, ExpenseError expenseError, Throwable cause) {
        super(message, expenseError, cause);
    }

    public ExpenseTrackerBadRequestException(ExpenseError expenseError, Throwable cause) {
        super(expenseError, cause);
    }
}
