package ru.java.filesharing.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.repository.FileRepository;
import ru.java.filesharing.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaFileRepository implements FileRepository {
    private final JpaFileRepositoryAdapter jpaFileRepositoryAdapter;
    private final UserRepository userRepository;

    @Override
    public Optional<File> findById(Long id) {
        Optional<File> fileOpt = jpaFileRepositoryAdapter.findById(id);
        fileOpt.ifPresent(this::enrichWithOwnerName);
        return fileOpt;
    }

    @Override
    public Optional<File> findByStorageKey(UUID storageKey) {
        Optional<File> fileOpt = jpaFileRepositoryAdapter.findByStorageKey(storageKey.toString());
        fileOpt.ifPresent(this::enrichWithOwnerName);
        return fileOpt;
    }

    @Override
    public List<File> findFilesByUserId(Long userId) {
        List<File> files = jpaFileRepositoryAdapter.findByOwnerId(userId);
        userRepository.findUsernameById(userId)
            .ifPresent(ownerName -> files.forEach(file -> file.setOwnerName(ownerName)));
        return files;
    }

    @Override
    public void create(File file) {
        jpaFileRepositoryAdapter.save(file);
    }

    @Override
    public void delete(Long id) {
        jpaFileRepositoryAdapter.deleteById(id);
    }

    private void enrichWithOwnerName(File file) {
        if (file.getOwnerName() == null && file.getOwnerId() != null) {
            userRepository.findUsernameById(file.getOwnerId())
                .ifPresent(file::setOwnerName);
        }
    }
}
