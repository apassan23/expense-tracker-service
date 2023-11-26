package com.phoenix.expensetrackerservice.converter;

import com.phoenix.expensetrackerservice.models.Payload;
import com.phoenix.expensetrackerservice.transform.PayloadBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class DefaultConverterTest {

    private DefaultConverter defaultConverter;

    @BeforeEach
    void setup() {
        defaultConverter = new DefaultConverter();
    }

    @Test
    void convertTest() {
        // prepare
        final String ACTION = "action";
        final Object[] ARGS = new Object[0];
        final Long EXECUTION_TIME = 100L;
        Payload payload = PayloadBuilder.build(ACTION, ARGS, EXECUTION_TIME, null);

        // Action & assert
        Payload convertedPayload = defaultConverter.convert(payload);
        Assertions.assertNotNull(convertedPayload);
        Assertions.assertEquals(ACTION, convertedPayload.getAction());
        Assertions.assertEquals(ARGS, convertedPayload.getArgs());
        Assertions.assertEquals(EXECUTION_TIME, convertedPayload.getExecutionTime());
        Assertions.assertNull(convertedPayload.getResponse());
    }

    @ParameterizedTest
    @NullSource
    void convertNullTest(Payload payload) {
        // Action & assert
        Assertions.assertThrows(NullPointerException.class, () -> defaultConverter.convert(payload));
    }
}