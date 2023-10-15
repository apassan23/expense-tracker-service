package com.phoenix.expensetrackerservice.util;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.transform.TransactionEntityBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionUtils {

    public static List<TransactionDTO> getTransactions(List<Transaction> transactions) {
        return transactions.stream().map(TransactionEntityBuilder::buildFromTransaction).toList();
    }
}
