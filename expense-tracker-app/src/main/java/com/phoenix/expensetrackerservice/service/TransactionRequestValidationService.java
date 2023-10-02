package com.phoenix.expensetrackerservice.service;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;

/**
 * @author abhishekpassan
 */
public interface TransactionRequestValidationService {
    /**
     * @param transactionDTO Transaction DTO
     */
    void validateForCreate(TransactionDTO transactionDTO);
    void validateForChange(TransactionDTO transactionDTO);
    void validateForRetrieve(TransactionDTO transactionDTO);

    void validateForRetrieve(RetrieveTransactionDTO retrieveTransactionDTO);
    void validateForDelete(TransactionDTO transactionDTO);
}
