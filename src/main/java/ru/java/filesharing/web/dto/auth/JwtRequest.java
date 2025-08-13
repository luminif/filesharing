package ru.java.filesharing.web.dto.auth;

import jakarta.validation.constraints.NotNull;

public record JwtRequest(
    @NotNull(message = "Username must not be null")
    String username,
    @NotNull(message = "Password must not be null")
    String password
) {
}