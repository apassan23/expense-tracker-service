package com.phoenix.expensetrackerservice.controller;

import com.phoenix.expensetrackerservice.model.TransactionDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransactionManagementAPI {
    ResponseEntity<TransactionDTO> createTransaction(TransactionDTO transaction);
    ResponseEntity<TransactionDTO> retrieveTransaction(String transactionId);

    ResponseEntity<List<TransactionDTO>> retrieveTransaction(Integer from, Integer pageSize);

    ResponseEntity<TransactionDTO> changeTransaction(TransactionDTO transactionDTO);
    ResponseEntity<Void> deleteTransaction(String transactionId);
}
