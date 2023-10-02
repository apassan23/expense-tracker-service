package com.phoenix.expensetrackerservice.exception;

import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExpenseTrackerException extends RuntimeException {
    private final ExpenseError expenseError;
    public ExpenseTrackerException(ExpenseError expenseError) {
        super();
        this.expenseError = expenseError;
    }

    public ExpenseTrackerException(String message, ExpenseError expenseError) {
        super(message);
        this.expenseError = expenseError;
    }

    public ExpenseTrackerException(String message, ExpenseError expenseError,  Throwable cause) {
        super(message, cause);
        this.expenseError = expenseError;
    }

    public ExpenseTrackerException(ExpenseError expenseError, Throwable cause) {
        super(cause);
        this.expenseError = expenseError;
    }
}
