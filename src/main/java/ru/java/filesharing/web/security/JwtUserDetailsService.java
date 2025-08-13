package ru.java.filesharing.web.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.java.filesharing.entity.user.User;
import ru.java.filesharing.exception.UserNotFoundException;
import ru.java.filesharing.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        User user = userService.getByUsername(username);
        //log.info(user.getUsername());
        //log.info(user.getPassword());
        return JwtEntityFactory.create(user);
    }
}
