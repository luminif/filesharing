package ru.java.filesharing.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.java.filesharing.entity.user.Role;
import ru.java.filesharing.entity.user.User;
import ru.java.filesharing.repository.UserRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {
    private final JpaUserRepositoryAdapter jpaUserRepositoryAdapter;

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepositoryAdapter.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepositoryAdapter.findByUsername(username);
    }

    @Override
    public void update(User user) {
        jpaUserRepositoryAdapter.save(user);
    }

    @Override
    public void create(User user) {
        jpaUserRepositoryAdapter.save(user);
    }

    @Override
    public void insertUserRole(Long userId, Role role) {

    }

    @Override
    public void delete(Long id) {
        jpaUserRepositoryAdapter.deleteById(id);
    }

    @Override
    public boolean isFileOwner(Long fileId, Long userId) {
        return jpaUserRepositoryAdapter.isFileOwner(fileId, userId);
    }
}
