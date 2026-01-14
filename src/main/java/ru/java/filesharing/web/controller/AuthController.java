package ru.java.filesharing.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.java.filesharing.service.AuthService;
import ru.java.filesharing.service.UserService;
import ru.java.filesharing.web.dto.auth.JwtRequest;
import ru.java.filesharing.web.dto.auth.JwtResponse;
import ru.java.filesharing.web.dto.user.request.CreateUserRequest;
import ru.java.filesharing.web.dto.user.response.CreateUserResponse;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@Tag(name = "Auth controller")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login")
    public JwtResponse login(@Validated @RequestBody JwtRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    @Operation(summary = "Register")
    public CreateUserResponse register(@Validated @RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
