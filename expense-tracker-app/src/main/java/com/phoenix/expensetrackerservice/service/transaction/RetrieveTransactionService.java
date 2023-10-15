package com.phoenix.expensetrackerservice.service.transaction;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;

import java.util.List;

public interface RetrieveTransactionService {
    List<TransactionDTO> given(RetrieveTransactionDTO retrieveTransactionDTO);
}
