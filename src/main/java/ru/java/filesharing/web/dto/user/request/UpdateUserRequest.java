package ru.java.filesharing.web.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateUserRequest(
    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 255, message = "Password must be between {min} and {max} characters long")
    String password
) {
}
