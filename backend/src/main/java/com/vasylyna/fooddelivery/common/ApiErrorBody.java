package com.vasylyna.fooddelivery.common;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ApiErrorBody {
    private ApiErrorBody() {
    }

    public static Map<String, Object> error(String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("error", message);
        return body;
    }

    public static Map<String, Object> validation(Map<String, String> fields) {
        Map<String, Object> body = error("Validation failed");
        body.put("fields", fields);
        return body;
    }
}