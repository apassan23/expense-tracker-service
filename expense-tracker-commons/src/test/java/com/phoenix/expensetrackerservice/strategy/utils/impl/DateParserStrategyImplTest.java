package com.phoenix.expensetrackerservice.strategy.utils.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.datetime.DateFormatter;

import java.util.Date;
import java.util.Locale;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DateParserStrategyImplTest {

    @Mock
    private DateFormatter dateFormatter;

    private DateParserStrategyImpl dateParserStrategy;

    @BeforeEach
    void setup() {
        dateParserStrategy = spy(new DateParserStrategyImpl(dateFormatter));
    }

    @Test
    void parseTest() throws Exception {
        // prepare
        Date date = new Date();
        String dateString = "23-02-1999";

        // mock
        when(dateFormatter.parse(dateString, Locale.getDefault())).thenReturn(date);

        // Action & assert
        Date actualDate = dateParserStrategy.parse(dateString);
        Assertions.assertEquals(date, actualDate);
    }
}