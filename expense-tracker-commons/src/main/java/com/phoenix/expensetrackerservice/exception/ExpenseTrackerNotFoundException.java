package com.phoenix.expensetrackerservice.exception;

import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;

public class ExpenseTrackerNotFoundException extends ExpenseTrackerException{
    public ExpenseTrackerNotFoundException(ExpenseError expenseError) {
        super(expenseError);
    }

    public ExpenseTrackerNotFoundException(String message, ExpenseError expenseError) {
        super(message, expenseError);
    }

    public ExpenseTrackerNotFoundException(String message, ExpenseError expenseError, Throwable cause) {
        super(message, expenseError, cause);
    }

    public ExpenseTrackerNotFoundException(ExpenseError expenseError, Throwable cause) {
        super(expenseError, cause);
    }
}
