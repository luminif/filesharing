package ru.java.filesharing.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.repository.FileRepository;
import ru.java.filesharing.web.mapper.FileRowMapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcFileRepository implements FileRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<File> findById(Long id) {
        String sql = """
            SELECT f.id AS file_id,
                   f.filename AS filename,
                   f.storage_key AS storage_key,
                   f.size_bytes AS size_bytes,
                   u.username AS owner_name,
                   f.upload_date AS upload_date,
                   f.is_public AS is_public,
                   f.owner_id AS owner_id
            FROM files f
            JOIN users u ON f.owner_id = u.id
            WHERE f.id = ?
            """;
        return jdbcTemplate.query(sql, new FileRowMapper(), id)
            .stream().findFirst();
    }

    @Override
    public Optional<File> findByStorageKey(UUID storageKey) {
        String sql = """
            SELECT f.id AS file_id,
                   f.filename AS filename,
                   f.storage_key AS storage_key,
                   f.size_bytes AS size_bytes,
                   u.username AS owner_name,
                   f.upload_date AS upload_date,
                   f.is_public AS is_public,
                   f.owner_id AS owner_id
            FROM files f
            JOIN users u ON f.owner_id = u.id
            WHERE f.storage_key = ?
            """;
        return jdbcTemplate.query(sql, new FileRowMapper(), storageKey.toString())
            .stream().findFirst();
    }

    @Override
    public List<File> findFilesByUserId(Long userId) {
        String sql = """
            SELECT f.id AS file_id,
                   f.filename AS filename,
                   f.storage_key AS storage_key,
                   f.size_bytes AS size_bytes,
                   u.username AS owner_name,
                   f.upload_date AS upload_date,
                   f.is_public AS is_public,
                   f.owner_id AS owner_id
            FROM files f
            JOIN users u ON f.owner_id = u.id
            WHERE f.owner_id = ?
            """;
        return jdbcTemplate.query(sql, new FileRowMapper(), userId);
    }

    @Override
    public void create(File file) {
        String sql = """
            INSERT INTO files (filename, storage_key, size_bytes, is_public, owner_id)
            VALUES (?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, file.getFileName());
            ps.setString(2, file.getStorageKey());
            ps.setLong(3, file.getSizeInBytes());
            ps.setBoolean(4, file.getIsPublic());
            ps.setLong(5, file.getOwnerId());
            return ps;
        }, keyHolder);

        file.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM files WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
