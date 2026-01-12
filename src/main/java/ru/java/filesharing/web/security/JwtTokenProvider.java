package ru.java.filesharing.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.java.filesharing.config.ApplicationConfig;
import ru.java.filesharing.entity.user.Role;
import ru.java.filesharing.entity.user.User;
import ru.java.filesharing.exception.AccessDeniedException;
import ru.java.filesharing.service.UserService;
import ru.java.filesharing.web.dto.auth.JwtResponse;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final ApplicationConfig config;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(config.security().secret().getBytes());
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        Claims claims = Jwts.claims()
            .subject(username)
            .add("id", userId)
            .add("roles", resolveRoles(roles))
            .build();
        Instant validity = Instant.now()
            .plus(config.security().access(), ChronoUnit.HOURS);
        return Jwts.builder()
            .claims(claims)
            .expiration(Date.from(validity))
            .signWith(key)
            .compact();
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
            .map(Enum::name)
            .collect(Collectors.toList());
    }

    public String createRefreshToken(Long userId, String username) {
        Claims claims = Jwts.claims()
            .subject(username)
            .add("id", userId)
            .build();
        Instant validity = Instant.now()
            .plus(config.security().refresh(), ChronoUnit.DAYS);
        return Jwts.builder()
            .claims(claims)
            .expiration(Date.from(validity))
            .signWith(key)
            .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {

        if (!isValid(refreshToken)) {
            throw new AccessDeniedException();
        }

        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);

        return new JwtResponse(
            userId,
            user.getUsername(),
            createAccessToken(userId, user.getUsername(), user.getRoles()),
            createRefreshToken(userId, user.getUsername())
        );
    }

    public boolean isValid(String token) {
        Jws<Claims> claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token);
        return claims.getPayload()
            .getExpiration()
            .after(new Date());
    }

    private String getId(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("id", String.class);
    }

    private String getUsername(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(
            username
        );
        return new UsernamePasswordAuthenticationToken(
            userDetails,
            "",
            userDetails.getAuthorities()
        );
    }

}
