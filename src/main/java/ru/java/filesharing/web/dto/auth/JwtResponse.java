package ru.java.filesharing.web.dto.auth;

public record JwtResponse(
    Long id,
    String login,
    String accessToken,
    String refreshToken
) {
}
