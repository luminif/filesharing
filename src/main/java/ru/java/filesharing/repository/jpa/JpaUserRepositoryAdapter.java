package ru.java.filesharing.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.java.filesharing.entity.user.User;

import java.util.Optional;

public interface JpaUserRepositoryAdapter extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM files
                WHERE id = :fileId AND owner_id = :userId
            )
            """, nativeQuery = true)
    boolean isFileOwner(Long fileId, Long userId);

    @Query(value = "SELECT u.username FROM users u WHERE u.id = :userId", nativeQuery = true)
    Optional<String> findUsernameById(Long userId);
}