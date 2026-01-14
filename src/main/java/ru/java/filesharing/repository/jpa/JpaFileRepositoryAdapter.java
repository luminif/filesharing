package ru.java.filesharing.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.java.filesharing.entity.file.File;

import java.util.List;
import java.util.Optional;

public interface JpaFileRepositoryAdapter extends JpaRepository<File, Long> {
    Optional<File> findByStorageKey(String storageKey);

    List<File> findByOwnerId(Long ownerId);
}
