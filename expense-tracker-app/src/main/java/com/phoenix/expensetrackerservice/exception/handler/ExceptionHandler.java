package com.phoenix.expensetrackerservice.exception.handler;

import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerNotFoundException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.exception.response.ErrorType;
import com.phoenix.expensetrackerservice.exception.response.ExpenseTrackerError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@SuppressWarnings("UnnecessaryLocalVariable")
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExpenseTrackerError handleException(Exception exception) {
        ExpenseTrackerError expenseTrackerError = new ExpenseTrackerError(ErrorType.INTERNAL_SERVER_ERROR, ExpenseError.SERVER_ERROR, exception.getMessage());
        return expenseTrackerError;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ExpenseTrackerException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExpenseTrackerError handleExpenseTrackerException(ExpenseTrackerException exception) {
        ExpenseTrackerError expenseTrackerError = new ExpenseTrackerError(ErrorType.INTERNAL_SERVER_ERROR, exception.getExpenseError(), exception.getMessage());
        return expenseTrackerError;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ExpenseTrackerBadRequestException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExpenseTrackerError handleBadRequestException(ExpenseTrackerException exception) {
        ExpenseTrackerError expenseTrackerError = new ExpenseTrackerError(ErrorType.BAD_REQUEST, exception.getExpenseError(), exception.getMessage());
        return expenseTrackerError;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ExpenseTrackerNotFoundException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExpenseTrackerError handleNotFoundException(ExpenseTrackerException exception) {
        ExpenseTrackerError expenseTrackerError = new ExpenseTrackerError(ErrorType.NOT_FOUND_ERROR, exception.getExpenseError(), exception.getMessage());
        return expenseTrackerError;
    }


}
