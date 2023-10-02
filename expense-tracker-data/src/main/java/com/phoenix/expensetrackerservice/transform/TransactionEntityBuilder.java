package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionEntityBuilder {

    public static Transaction buildFromTransactionDto(TransactionDTO transactionDTO){
        return Transaction.builder()
                .transactionId(transactionDTO.getTransactionId())
                .transactionName(transactionDTO.getTransactionName())
                .transactionDate(transactionDTO.getTransactionDate())
                .transactionMonth(transactionDTO.getTransactionMonth())
                .transactionType(transactionDTO.getTransactionType())
                .notes(transactionDTO.getNotes())
                .amount(transactionDTO.getAmount())
                .categoryId(transactionDTO.getCategoryId())
                .account(transactionDTO.getAccount())
                .build();
    }

    public static TransactionDTO buildFromTransaction(Transaction transaction) {
        return TransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .transactionName(transaction.getTransactionName())
                .transactionDate(transaction.getTransactionDate())
                .transactionMonth(transaction.getTransactionMonth())
                .transactionType(transaction.getTransactionType())
                .notes(transaction.getNotes())
                .amount(transaction.getAmount())
                .categoryId(transaction.getCategoryId())
                .account(transaction.getAccount())
                .build();
    }
}
