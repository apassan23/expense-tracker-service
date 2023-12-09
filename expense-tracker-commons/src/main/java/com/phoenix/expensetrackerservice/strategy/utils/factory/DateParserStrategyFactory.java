package com.phoenix.expensetrackerservice.strategy.utils.factory;

import com.phoenix.expensetrackerservice.strategy.utils.DateParserStrategy;
import com.phoenix.expensetrackerservice.strategy.utils.impl.DateParserStrategyImpl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.format.datetime.DateFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateParserStrategyFactory {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static DateParserStrategy withDefaultPattern() {
        DateFormatter dateFormatter = new DateFormatter(DATE_FORMAT);
        return new DateParserStrategyImpl(dateFormatter);
    }

    public static DateParserStrategy withPattern(String pattern) {
        DateFormatter dateFormatter = new DateFormatter(pattern);
        return new DateParserStrategyImpl(dateFormatter);
    }
}
