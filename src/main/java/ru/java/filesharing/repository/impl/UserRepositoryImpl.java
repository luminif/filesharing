package ru.java.filesharing.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.java.filesharing.entity.user.Role;
import ru.java.filesharing.entity.user.User;
import ru.java.filesharing.repository.UserRepository;
import ru.java.filesharing.web.mapper.UserRowMapper;

import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findById(Long id) {
        String sql = """
            SELECT u.id AS user_id,
                   u.username AS username,
                   u.password AS password,
                   u.created_at AS created_at,
                   ur.role AS user_role,
                   f.id AS file_id,
                   f.filename AS filename,
                   f.storage_key AS storage_key,
                   f.size_bytes AS size_bytes,
                   f.upload_date AS upload_date,
                   f.is_public AS is_public,
                   f.owner_id AS owner_id,
                   u.username AS owner_name
            FROM users u
                LEFT JOIN user_roles ur ON u.id = ur.user_id
                LEFT JOIN files f ON u.id = f.owner_id
            WHERE u.id = ?
            """;

        return Optional.ofNullable(jdbcTemplate.query(sql, new UserRowMapper(), id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = """
            SELECT u.id AS user_id,
                   u.username AS username,
                   u.password AS password,
                   u.created_at AS created_at,
                   ur.role AS user_role,
                   f.id AS file_id,
                   f.filename AS filename,
                   f.storage_key AS storage_key,
                   f.size_bytes AS size_bytes,
                   f.upload_date AS upload_date,
                   f.is_public AS is_public,
                   f.owner_id AS owner_id,
                   u.username AS owner_name
            FROM users u
                LEFT JOIN user_roles ur ON u.id = ur.user_id
                LEFT JOIN files f ON u.id = f.owner_id
            WHERE u.username = ?
            """;

        return Optional.ofNullable(jdbcTemplate.query(sql, new UserRowMapper(), username));
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getId());
    }

    @Override
    public void create(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        String sql = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, role.name());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean isFileOwner(Long fileId, Long userId) {
        String sql = """
            SELECT EXISTS (
                SELECT 1
                FROM files
                WHERE id = ? AND owner_id = ?
            )
            """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, fileId, userId);
    }
}
