package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveTransactionBuilder {

    public static RetrieveTransactionDTO build(Integer pageNumber, Integer pageSize, boolean fetchAll) {
        return build(null, pageNumber, pageSize, fetchAll);
    }

    public static RetrieveTransactionDTO build(Integer pageNumber, Integer pageSize) {
        return build(null, pageNumber, pageSize, Boolean.FALSE);
    }

    public static RetrieveTransactionDTO build(String transactionId, Integer pageNumber, Integer pageSize, boolean fetchAll) {
        return RetrieveTransactionDTO.builder()
                .transactionId(transactionId)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .fetchAll(fetchAll)
                .build();
    }

    public static RetrieveTransactionDTO build(String transactionId) {
        return build(transactionId, null, null, Boolean.FALSE);
    }
}
