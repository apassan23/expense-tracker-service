package com.phoenix.expensetrackerservice.strategy.transaction;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;

import java.util.List;

public interface RetrieveTransactionStrategy {
    List<TransactionDTO> retrieve(RetrieveTransactionDTO retrieveTransactionDTO);

    RetrieveType retrieveType();
}
