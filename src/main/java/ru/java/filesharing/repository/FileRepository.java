package ru.java.filesharing.repository;

import ru.java.filesharing.entity.file.File;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository {
    Optional<File> findById(Long id);

    Optional<File> findByStorageKey(UUID storageKey);

    List<File> findFilesByUserId(Long userId);

    void create(File file);

    void delete(Long id);
}
