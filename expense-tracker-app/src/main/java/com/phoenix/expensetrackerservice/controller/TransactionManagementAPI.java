package com.phoenix.expensetrackerservice.controller;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransactionManagementAPI {
    ResponseEntity<TransactionDTO> createTransaction(TransactionDTO transaction);

    ResponseEntity<TransactionDTO> retrieveTransaction(String transactionId);

    ResponseEntity<List<TransactionDTO>> retrieveTransaction(RetrieveTransactionDTO retrieveTransactionDTO);

    ResponseEntity<TransactionDTO> changeTransaction(TransactionDTO transactionDTO);

    ResponseEntity<Void> deleteTransaction(String transactionId);
}
