package com.phoenix.expensetrackerservice.utils;

import com.phoenix.expensetrackerservice.strategy.utils.DateParserStrategy;
import com.phoenix.expensetrackerservice.strategy.utils.factory.DateParserStrategyFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    private static final DateParserStrategy dateParserStrategy;

    static {
        dateParserStrategy = DateParserStrategyFactory.withDefaultPattern();
    }

    public static Date parse(String date) throws Exception {
        return dateParserStrategy.parse(date);
    }

    public static Date nextDay(Date date) {
        Instant nextDayInstant = date.toInstant().plus(1, ChronoUnit.DAYS);
        return Date.from(nextDayInstant);
    }
}
