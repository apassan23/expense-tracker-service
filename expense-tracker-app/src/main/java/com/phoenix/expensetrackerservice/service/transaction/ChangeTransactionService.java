package com.phoenix.expensetrackerservice.service.transaction;

import com.phoenix.expensetrackerservice.model.TransactionDTO;

public interface ChangeTransactionService {
    TransactionDTO given(TransactionDTO transactionDTO);
}
