package ru.java.filesharing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.java.filesharing.constants.Constants;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.exception.FileNotFoundException;
import ru.java.filesharing.repository.FileRepository;
import ru.java.filesharing.service.FileService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    @Override
    @Transactional(readOnly = true)
    public File getById(Long id) {
        return fileRepository.findById(id)
            .orElseThrow(() -> new FileNotFoundException(Constants.FILE_NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional(readOnly = true)
    public File getByStorageKey(UUID storageKey) {
        return fileRepository.findByStorageKey(storageKey)
            .orElseThrow(() -> new FileNotFoundException(Constants.FILE_NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional(readOnly = true)
    public List<File> getFilesByUserId(Long userId) {
        return fileRepository.findFilesByUserId(userId);
    }

    @Override
    @Transactional
    public File create(File file, MultipartFile multipartFile) {
        String storageKey = UUID.randomUUID().toString();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setStorageKey(storageKey);
        file.setSizeInBytes(multipartFile.getSize());
        fileRepository.create(file);
        return file;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        fileRepository.delete(id);
    }
}
