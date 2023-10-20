package com.phoenix.expensetrackerservice.strategy.utils;

import java.util.Date;

public interface DateParserStrategy {
    Date parse(String date) throws Exception;
}
