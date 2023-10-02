package com.phoenix.expensetrackerservice.entity;

import com.phoenix.expensetrackerservice.enums.Account;
import com.phoenix.expensetrackerservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("transactions")
public class Transaction {
    @Id
    private String transactionId;

    private String transactionName;

    private BigDecimal amount;

    private String categoryId;

    @CreatedDate
    private Date transactionDate;

    private String transactionMonth;

    private Account account;

    private TransactionType transactionType;

    private String notes;
}
