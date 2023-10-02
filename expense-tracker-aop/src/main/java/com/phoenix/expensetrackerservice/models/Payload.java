package com.phoenix.expensetrackerservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payload implements Serializable {
    private String action;
    private Long executionTime;
    private Object[] args;
    private Object response;
}
