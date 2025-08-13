package ru.java.filesharing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.java.filesharing.entity.user.User;
import ru.java.filesharing.service.AuthService;
import ru.java.filesharing.service.UserService;
import ru.java.filesharing.web.dto.auth.JwtRequest;
import ru.java.filesharing.web.dto.auth.JwtResponse;
import ru.java.filesharing.web.security.JwtTokenProvider;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
            )
        );

        User user = userService.getByUsername(loginRequest.username());

        //log.info(user.toString());

        String accessToken = jwtTokenProvider.createAccessToken(
            user.getId(),
            user.getUsername(),
            user.getRoles()
        );

        //log.info(accessToken);

        String refreshToken = jwtTokenProvider.createRefreshToken(
            user.getId(),
            user.getUsername()
        );

        //log.info(refreshToken);

        return new JwtResponse(
            user.getId(),
            user.getUsername(),
            accessToken,
            refreshToken
        );
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
