package com.phoenix.expensetrackerservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static String getJsonString(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public static Map<String, Object> parse(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

    public static <T> T parse(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }
}
