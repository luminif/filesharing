package ru.java.filesharing.web.dto.user.response;

import java.time.LocalDateTime;

public record CreateUserResponse(
    Long id,
    String username,
    LocalDateTime createdAt
) {
}
