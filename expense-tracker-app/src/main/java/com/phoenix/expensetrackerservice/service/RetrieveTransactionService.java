package com.phoenix.expensetrackerservice.service;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;

import java.util.List;

public interface RetrieveTransactionService {
    TransactionDTO given(TransactionDTO transactionDTO);
    List<TransactionDTO> given(RetrieveTransactionDTO retrieveTransactionDTO);
}
