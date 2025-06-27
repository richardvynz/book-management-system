package com.richardvinz.Book_Management_App.testUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    public static <T> T fromJsonString(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to object", e);
        }
    }

    // Fixed: Add method to properly handle Map conversion
    public static Map<String, Object> fromJsonStringToMap(String json) {
        try {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to Map", e);
        }
    }

    // Alternative method to extract ID safely
    public static Long extractIdFromResponse(String jsonResponse) {
        try {
            Map<String, Object> responseMap = fromJsonStringToMap(jsonResponse);
            Object idValue = responseMap.get("id");

            if (idValue instanceof Integer) {
                return ((Integer) idValue).longValue();
            } else if (idValue instanceof Long) {
                return (Long) idValue;
            } else if (idValue instanceof String) {
                return Long.parseLong((String) idValue);
            } else {
                throw new RuntimeException("Unable to extract ID from response");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error extracting ID from JSON response", e);
        }
    }
}