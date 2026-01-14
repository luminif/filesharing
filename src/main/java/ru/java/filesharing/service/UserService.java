package ru.java.filesharing.service;

import ru.java.filesharing.entity.user.User;
import ru.java.filesharing.web.dto.user.request.CreateUserRequest;
import ru.java.filesharing.web.dto.user.request.UpdateUserRequest;
import ru.java.filesharing.web.dto.user.response.CreateUserResponse;
import ru.java.filesharing.web.dto.user.response.GetUserResponse;
import ru.java.filesharing.web.dto.user.response.UpdateUserResponse;

public interface UserService {
    GetUserResponse getById(Long id);

    User getByUsername(String username);

    UpdateUserResponse update(Long id, UpdateUserRequest request);

    CreateUserResponse create(CreateUserRequest request);

    String getUsernameById(Long userId);

    void delete(Long id);

    boolean isFileOwner(Long fileId, Long userId);
}
