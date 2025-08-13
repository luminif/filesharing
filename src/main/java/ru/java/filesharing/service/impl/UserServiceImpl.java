package ru.java.filesharing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.filesharing.constants.Constants;
import ru.java.filesharing.entity.user.Role;
import ru.java.filesharing.entity.user.User;
import ru.java.filesharing.exception.UserAlreadyExistsException;
import ru.java.filesharing.exception.UserNotFoundException;
import ru.java.filesharing.repository.UserRepository;
import ru.java.filesharing.service.UserService;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional
    public User update(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.update(user);
        return userRepository.findById(user.getId()).get();
    }

    @Override
    @Transactional
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(Constants.USER_ALREADY_EXISTS_MESSAGE);
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.create(user);
        userRepository.insertUserRole(user.getId(), Role.ROLE_USER);
        user.setRoles(Set.of(Role.ROLE_USER));
        return userRepository.findById(user.getId()).get();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public boolean isFileOwner(Long fileId, Long userId) {
        return userRepository.isFileOwner(fileId, userId);
    }
}
