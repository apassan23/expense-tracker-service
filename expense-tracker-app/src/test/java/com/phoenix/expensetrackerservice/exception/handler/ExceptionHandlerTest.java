package com.phoenix.expensetrackerservice.exception.handler;

import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.exception.response.ErrorType;
import com.phoenix.expensetrackerservice.exception.response.ExpenseTrackerError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.spy;

class ExceptionHandlerTest {

    private ExceptionHandler exceptionHandler;

    @BeforeEach
    void setup() {
        exceptionHandler = spy(new ExceptionHandler());
    }

    @Test
    void handleException() {
        // prepare
        RuntimeException exception = new RuntimeException();

        ExpenseTrackerError expenseTrackerError = exceptionHandler.handleException(exception);
        Assertions.assertNotNull(expenseTrackerError);
        Assertions.assertEquals(ErrorType.INTERNAL_SERVER_ERROR, expenseTrackerError.getErrorType());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, expenseTrackerError.getErrorCode());
    }

    @Test
    void handleExpenseTrackerException() {
        // prepare
        ExpenseTrackerException exception = new ExpenseTrackerException(ExpenseError.SERVER_ERROR);

        ExpenseTrackerError expenseTrackerError = exceptionHandler.handleExpenseTrackerException(exception);
        Assertions.assertNotNull(expenseTrackerError);
        Assertions.assertEquals(ErrorType.INTERNAL_SERVER_ERROR, expenseTrackerError.getErrorType());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, expenseTrackerError.getErrorCode());
    }

    @Test
    void handleBadRequestException() {
        // prepare
        ExpenseTrackerBadRequestException exception = new ExpenseTrackerBadRequestException(ExpenseError.BAD_REQUEST);

        ExpenseTrackerError expenseTrackerError = exceptionHandler.handleBadRequestException(exception);
        Assertions.assertNotNull(expenseTrackerError);
        Assertions.assertEquals(ErrorType.BAD_REQUEST, expenseTrackerError.getErrorType());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, expenseTrackerError.getErrorCode());
    }

    @Test
    void handleNotFoundException() {
        // prepare
        ExpenseTrackerNotFoundException exception = new ExpenseTrackerNotFoundException(ExpenseError.TRANSACTION_NOT_PRESENT);

        ExpenseTrackerError expenseTrackerError = exceptionHandler.handleNotFoundException(exception);
        Assertions.assertNotNull(expenseTrackerError);
        Assertions.assertEquals(ErrorType.NOT_FOUND_ERROR, expenseTrackerError.getErrorType());
        Assertions.assertEquals(ExpenseError.TRANSACTION_NOT_PRESENT, expenseTrackerError.getErrorCode());
    }
}