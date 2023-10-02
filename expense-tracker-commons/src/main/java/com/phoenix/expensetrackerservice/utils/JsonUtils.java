package com.phoenix.expensetrackerservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static String getJsonString(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
