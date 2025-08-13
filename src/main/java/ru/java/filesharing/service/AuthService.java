package ru.java.filesharing.service;

import ru.java.filesharing.web.dto.auth.JwtRequest;
import ru.java.filesharing.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest request);

    JwtResponse refresh(String refreshToken);
}
