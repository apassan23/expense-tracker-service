package com.phoenix.expensetrackerservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.phoenix.expensetrackerservice.enums.Account;
import com.phoenix.expensetrackerservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDTO {
    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("transactionName")
    private String transactionName;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("categoryId")
    private String categoryId;

    @JsonProperty("transactionDate")
    private Date transactionDate;

    @JsonProperty("transactionMonth")
    private String transactionMonth;

    @JsonProperty("account")
    private Account account;

    @JsonProperty("transactionType")
    private TransactionType transactionType;

    @JsonProperty("notes")
    private String notes;
}
