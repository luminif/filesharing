package ru.java.filesharing.web.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import ru.java.filesharing.constants.Constants;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @SneakyThrows
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = extractTokenFromHeader(request);

        //String req = request.getReader().lines().collect(Collectors.joining());

        //log.info("req: " + req);
        //log.info("token: " + token);

        try {
            if (token != null && jwtTokenProvider.isValid(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                if (authentication != null) {
                    SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (SignatureException | ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(Constants.UNAUTHORIZED);
        }
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(Constants.AUTHORIZATION);

        if (header != null && header.startsWith(Constants.BEARER_PREFIX)) {
            return header.substring(Constants.BEARER_PREFIX.length());
        }

        return null;
    }
}
