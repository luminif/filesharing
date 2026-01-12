package ru.java.filesharing.repository;

import ru.java.filesharing.entity.user.Role;
import ru.java.filesharing.entity.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    void update(User user);

    void create(User user);

    void insertUserRole(Long userId, Role role);

    void delete(Long id);

    boolean isFileOwner(Long fileId, Long userId);
}
