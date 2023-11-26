package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.models.Payload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PayloadBuilderTest {

    @Test
    void buildTest() {
        // prepare
        final String ACTION = "action";
        final Object[] ARGS = new Object[0];
        final Long EXECUTION_TIME = 100L;

        // Action & assert
        Payload payload = PayloadBuilder.build(ACTION, ARGS, EXECUTION_TIME, null);
        Assertions.assertNotNull(payload);
        Assertions.assertEquals(ACTION, payload.getAction());
        Assertions.assertEquals(ARGS, payload.getArgs());
        Assertions.assertEquals(EXECUTION_TIME, payload.getExecutionTime());
        Assertions.assertNull(payload.getResponse());
    }
}