package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.model.RetrieveTransactionDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveTransactionBuilder {

    public static RetrieveTransactionDTO build(Integer pageNumber, Integer pageSize, boolean fetchAll) {
        RetrieveTransactionDTO retrieveTransactionDTO = build(pageNumber, pageSize);
        retrieveTransactionDTO.setFetchAll(fetchAll);
        return retrieveTransactionDTO;
    }

    public static RetrieveTransactionDTO build(Integer pageNumber, Integer pageSize) {
        return RetrieveTransactionDTO.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
    }
}
