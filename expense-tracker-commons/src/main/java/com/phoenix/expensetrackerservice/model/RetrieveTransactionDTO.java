package com.phoenix.expensetrackerservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveTransactionDTO {
    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("date")
    private String date;

    @JsonProperty("pageNumber")
    private Integer pageNumber;

    @JsonProperty("pageSize")
    private Integer pageSize;

    @JsonIgnore
    @Builder.Default
    private boolean fetchAll = Boolean.TRUE;
}
