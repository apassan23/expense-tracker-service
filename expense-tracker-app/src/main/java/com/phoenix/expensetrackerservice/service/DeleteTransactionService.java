package com.phoenix.expensetrackerservice.service;

import com.phoenix.expensetrackerservice.model.TransactionDTO;

public interface DeleteTransactionService {
    TransactionDTO given(TransactionDTO transactionDTO);
}
