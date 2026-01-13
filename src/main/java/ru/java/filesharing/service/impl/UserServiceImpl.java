package ru.java.filesharing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.filesharing.constants.Constants;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.entity.user.Role;
import ru.java.filesharing.entity.user.User;
import ru.java.filesharing.exception.FileDeleteException;
import ru.java.filesharing.exception.UserAlreadyExistsException;
import ru.java.filesharing.exception.UserNotFoundException;
import ru.java.filesharing.repository.FileRepository;
import ru.java.filesharing.repository.UserRepository;
import ru.java.filesharing.service.MinioService;
import ru.java.filesharing.service.UserService;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final FileRepository fileRepository;
    private final MinioService minioService;

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
        User existingUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
        existingUser.setPassword(encoder.encode(user.getPassword()));
        userRepository.update(existingUser);
        return existingUser;
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
        return userRepository.findById(user.getId())
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional(readOnly = true)
    public String getUsernameById(Long userId) {
        return userRepository.findUsernameById(userId)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        List<File> files = fileRepository.findFilesByUserId(id);

        try {
            files.forEach(file -> minioService.delete(file.getStorageKey()));
            userRepository.delete(id);
        } catch (Exception e) {
            throw new FileDeleteException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFileOwner(Long fileId, Long userId) {
        return userRepository.isFileOwner(fileId, userId);
    }
}
