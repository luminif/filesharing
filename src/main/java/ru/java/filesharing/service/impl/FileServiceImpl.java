package ru.java.filesharing.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.filesharing.constants.Constants;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.exception.FileDeleteException;
import ru.java.filesharing.exception.FileNotFoundException;
import ru.java.filesharing.exception.FileUploadException;
import ru.java.filesharing.repository.FileRepository;
import ru.java.filesharing.service.FileService;
import ru.java.filesharing.service.MinioService;
import ru.java.filesharing.web.dto.file.request.CreateFileRequest;
import ru.java.filesharing.web.dto.file.response.CreateFileResponse;
import ru.java.filesharing.web.dto.file.response.GetFileResponse;
import ru.java.filesharing.web.mapper.FileMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final MinioService minioService;
    private final FileMapper fileMapper;

    @Override
    @Transactional(readOnly = true)
    public GetFileResponse getById(Long id) {
        File file = fileRepository.findById(id)
            .orElseThrow(() -> new FileNotFoundException(Constants.FILE_NOT_FOUND_MESSAGE));
        return mapToGetFileResponse(file);
    }

    @Override
    @Transactional(readOnly = true)
    public GetFileResponse getByStorageKey(UUID storageKey) {
        File file = fileRepository.findByStorageKey(storageKey)
            .orElseThrow(() -> new FileNotFoundException(Constants.FILE_NOT_FOUND_MESSAGE));
        return mapToGetFileResponse(file);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetFileResponse> getFilesByUserId(Long userId) {
        List<File> files = fileRepository.findFilesByUserId(userId);
        return files.stream()
            .map(this::mapToGetFileResponse)
            .toList();
    }

    @Override
    @Transactional
    public CreateFileResponse create(Long ownerId, CreateFileRequest request) {
        String fileName = FilenameUtils.getBaseName(request.file().getOriginalFilename());
        String storageKey = fileName + "-" + UUID.randomUUID();

        try {
            minioService.upload(
                storageKey,
                request.file().getInputStream(),
                request.file().getSize(),
                request.file().getContentType()
            );

            File file = new File();
            file.setFileName(request.file().getOriginalFilename());
            file.setStorageKey(storageKey);
            file.setSizeInBytes(request.file().getSize());
            file.setOwnerId(ownerId);
            file.setIsPublic(request.isPublic());
            
            fileRepository.create(file);

            File savedFile = fileRepository.findById(file.getId())
                .orElseThrow(() -> new FileNotFoundException(Constants.FILE_NOT_FOUND_MESSAGE));
            return mapToCreateFileResponse(savedFile);
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        File file = fileRepository.findById(id)
            .orElseThrow(() -> new FileNotFoundException(Constants.FILE_NOT_FOUND_MESSAGE));

        try {
            minioService.delete(file.getStorageKey());
            fileRepository.delete(id);
        } catch (Exception e) {
            throw new FileDeleteException(e.getMessage());
        }
    }

    private GetFileResponse mapToGetFileResponse(File file) {
        String downloadUrl = minioService.getPresignedUrl(file.getStorageKey());
        return fileMapper.mapToGetFileResponse(file, downloadUrl);
    }

    private CreateFileResponse mapToCreateFileResponse(File file) {
        String downloadUrl = minioService.getPresignedUrl(file.getStorageKey());
        return fileMapper.mapToCreateFileResponse(file, downloadUrl);
    }
}
