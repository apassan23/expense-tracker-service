package com.phoenix.expensetrackerservice.strategy;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;

import java.util.List;

public interface RetrieveStrategy {
    List<TransactionDTO> retrieve(RetrieveTransactionDTO retrieveTransactionDTO);

    RetrieveType retrieveType();
}
