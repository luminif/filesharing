package ru.java.filesharing.web.dto.error;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ApiErrorResponse(
    int statusCode,
    String message,
    Map<String, String> errors
) {
    public ApiErrorResponse(HttpStatus status, String message, Map<String, String> errors) {
        this(status.value(), message, Map.copyOf(errors));
    }

    public ApiErrorResponse(HttpStatus status, String message) {
        this(status.value(), message, Map.of());
    }
}
