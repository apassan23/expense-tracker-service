package com.phoenix.expensetrackerservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockedTransactionDetails {
    private String transactionId;
    private String categoryId;
}
