package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.model.TransactionDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionBuilder {

    public static TransactionDTO buildFromTransactionId(String transactionId){
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(transactionId);
        return transactionDTO;
    }
}
