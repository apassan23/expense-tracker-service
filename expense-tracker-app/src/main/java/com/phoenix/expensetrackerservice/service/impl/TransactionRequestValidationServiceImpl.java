package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.service.TransactionRequestValidationService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TransactionRequestValidationServiceImpl implements TransactionRequestValidationService {
    @Override
    public void validateForCreate(TransactionDTO transactionDTO) {
        if(Objects.nonNull(transactionDTO) &&
                Objects.nonNull(transactionDTO.getTransactionName()) &&
                Objects.nonNull(transactionDTO.getTransactionMonth()) &&
                Objects.nonNull(transactionDTO.getTransactionType()) &&
                Objects.nonNull(transactionDTO.getAmount()) &&
                Objects.nonNull(transactionDTO.getAccount()) &&
                Objects.nonNull(transactionDTO.getCategoryId()) &&
                Objects.nonNull(transactionDTO.getNotes())
        ) {
            return;
        }

        throw new ExpenseTrackerBadRequestException(ErrorConstants.BAD_REQUEST_MESSAGE, ExpenseError.BAD_REQUEST);
    }

    @Override
    public void validateForChange(TransactionDTO transactionDTO) {
        if(Objects.nonNull(transactionDTO) &&
                Objects.nonNull(transactionDTO.getTransactionId()) &&
                Objects.nonNull(transactionDTO.getTransactionName()) &&
                Objects.nonNull(transactionDTO.getTransactionMonth()) &&
                Objects.nonNull(transactionDTO.getTransactionType()) &&
                Objects.nonNull(transactionDTO.getAmount()) &&
                Objects.nonNull(transactionDTO.getAccount()) &&
                Objects.nonNull(transactionDTO.getCategoryId()) &&
                Objects.nonNull(transactionDTO.getNotes())
        ) {
            return;
        }

        throw new ExpenseTrackerBadRequestException(ErrorConstants.BAD_REQUEST_MESSAGE, ExpenseError.BAD_REQUEST);
    }

    @Override
    public void validateForRetrieve(TransactionDTO transactionDTO) {
        if(Objects.nonNull(transactionDTO) &&
                Objects.nonNull(transactionDTO.getTransactionId())) {
            return;
        }

        throw new ExpenseTrackerBadRequestException(ErrorConstants.BAD_REQUEST_MESSAGE, ExpenseError.BAD_REQUEST);
    }

    @Override
    public void validateForRetrieve(RetrieveTransactionDTO retrieveTransactionDTO) {
        if(Objects.nonNull(retrieveTransactionDTO)){
            if(Objects.isNull(retrieveTransactionDTO.getPageSize()) && Objects.isNull(retrieveTransactionDTO.getPageNumber())) {
                return;
            } else if (Objects.nonNull(retrieveTransactionDTO.getPageSize()) &&
                    Objects.nonNull(retrieveTransactionDTO.getPageNumber()) &&
                    retrieveTransactionDTO.getPageSize() >= 1 &&
                    retrieveTransactionDTO.getPageNumber() >= 0) {
                return;
            }
        }
        throw new ExpenseTrackerBadRequestException(ErrorConstants.BAD_REQUEST_MESSAGE, ExpenseError.BAD_REQUEST);
    }

    @Override
    public void validateForDelete(TransactionDTO transactionDTO) {
        if(Objects.nonNull(transactionDTO) &&
                Objects.nonNull(transactionDTO.getTransactionId())) {
            return;
        }

        throw new ExpenseTrackerBadRequestException(ErrorConstants.BAD_REQUEST_MESSAGE, ExpenseError.BAD_REQUEST);
    }
}
