package ru.java.filesharing.service;

import ru.java.filesharing.entity.user.User;

public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    User update(User user);

    User create(User user);

    void delete(Long id);

    boolean isFileOwner(Long fileId, Long userId);
}
