package ru.java.filesharing.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.repository.FileRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaFileRepository implements FileRepository {
    private final JpaFileRepositoryAdapter jpaFileRepositoryAdapter;

    @Override
    public Optional<File> findById(Long id) {
        return jpaFileRepositoryAdapter.findById(id);
    }

    @Override
    public Optional<File> findByStorageKey(UUID storageKey) {
        return jpaFileRepositoryAdapter.findByStorageKey(storageKey.toString());
    }

    @Override
    public List<File> findFilesByUserId(Long userId) {
        return jpaFileRepositoryAdapter.findByOwnerId(userId);
    }

    @Override
    public void create(File file) {
        jpaFileRepositoryAdapter.save(file);
    }

    @Override
    public void delete(Long id) {
        jpaFileRepositoryAdapter.deleteById(id);
    }
}
