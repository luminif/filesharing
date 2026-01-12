package ru.java.filesharing.service;

import org.springframework.web.multipart.MultipartFile;
import ru.java.filesharing.entity.file.File;

import java.util.List;
import java.util.UUID;

public interface FileService {
    File getById(Long id);

    File getByStorageKey(UUID storageKey);

    List<File> getFilesByUserId(Long userId);

    File create(File file, MultipartFile multipartFile);

    void delete(Long id);
}
