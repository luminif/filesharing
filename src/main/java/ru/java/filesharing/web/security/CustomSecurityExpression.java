package ru.java.filesharing.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.java.filesharing.entity.user.Role;
import ru.java.filesharing.service.UserService;

@Component
@RequiredArgsConstructor
public class CustomSecurityExpression {
    private final UserService userService;

    public boolean canAccessUser(Long id) {
        Authentication auth = getAuth();
        JwtEntity user = (JwtEntity) auth.getPrincipal();
        Long userId = user.getId();
        return userId.equals(id) || hasAdminRole(auth);
    }

    public boolean canAccessFile(Long id) {
        Authentication auth = getAuth();
        JwtEntity user = (JwtEntity) auth.getPrincipal();
        Long userId = user.getId();
        return userService.isFileOwner(id, userId) || hasAdminRole(auth);
    }

    private boolean hasAdminRole(Authentication auth) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Role.ROLE_ADMIN.name());
        return auth.getAuthorities().contains(authority);
    }

    private Authentication getAuth() {
       return SecurityContextHolder.getContext()
           .getAuthentication();
    }
}
