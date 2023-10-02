package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.models.Payload;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PayloadBuilder {

    public static Payload build(String action, Object[] args, Long executionTime, Object response) {
        Payload payload = new Payload();
        payload.setAction(action);
        payload.setArgs(args);
        payload.setExecutionTime(executionTime);
        payload.setResponse(response);
        return payload;
    }
}
