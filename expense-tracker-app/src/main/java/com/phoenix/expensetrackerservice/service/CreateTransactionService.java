package com.phoenix.expensetrackerservice.service;

import com.phoenix.expensetrackerservice.model.TransactionDTO;

public interface CreateTransactionService {
    TransactionDTO given(TransactionDTO transactionDTO);
}
