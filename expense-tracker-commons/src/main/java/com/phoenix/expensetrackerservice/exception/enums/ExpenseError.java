package com.phoenix.expensetrackerservice.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExpenseError {
    BAD_REQUEST("Bad Request."),
    TRANSACTION_CREATE_ALREADY_EXISTS("Transaction Already Exists."),
    CATEGORY_DOES_NOT_EXISTS("Category does not exists."),
    TRANSACTION_NOT_PRESENT("Transaction not present."),
    CATEGORY_ALREADY_EXISTS("Category Already Exists."),
    SERVER_ERROR("Server Error.")
    ;

    private String description;

}
