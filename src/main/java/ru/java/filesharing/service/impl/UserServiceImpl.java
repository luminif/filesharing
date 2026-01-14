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
import ru.java.filesharing.web.dto.user.request.CreateUserRequest;
import ru.java.filesharing.web.dto.user.request.UpdateUserRequest;
import ru.java.filesharing.web.dto.user.response.CreateUserResponse;
import ru.java.filesharing.web.dto.user.response.GetUserResponse;
import ru.java.filesharing.web.dto.user.response.UpdateUserResponse;
import ru.java.filesharing.web.mapper.UserMapper;

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
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public GetUserResponse getById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
        return userMapper.mapToGetUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional
    public UpdateUserResponse update(Long id, UpdateUserRequest request) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));

        User updateData = userMapper.mapFromUpdateUserRequestToEntity(request);
        existingUser.setPassword(encoder.encode(updateData.getPassword()));
        userRepository.update(existingUser);
        return userMapper.mapToUpdateUserResponse(existingUser);
    }

    @Override
    @Transactional
    public CreateUserResponse create(CreateUserRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UserAlreadyExistsException(Constants.USER_ALREADY_EXISTS_MESSAGE);
        }

        User user = userMapper.mapFromCreateUserRequestToEntity(request);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.create(user);
        userRepository.insertUserRole(user.getId(), Role.ROLE_USER);
        user.setRoles(Set.of(Role.ROLE_USER));
        
        User createdUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE));
        return userMapper.mapToCreateUserResponse(createdUser);
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
