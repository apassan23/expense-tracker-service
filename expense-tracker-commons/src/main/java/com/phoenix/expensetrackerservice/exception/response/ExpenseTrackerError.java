package com.phoenix.expensetrackerservice.exception.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseTrackerError {
    @JsonProperty("errorType")
    private ErrorType errorType;

    @JsonProperty("errorCode")
    private ExpenseError errorCode;

    @JsonProperty("errorDescription")
    private String errorDescription;
}
