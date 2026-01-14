package ru.java.filesharing.service;

import ru.java.filesharing.web.dto.file.request.CreateFileRequest;
import ru.java.filesharing.web.dto.file.response.CreateFileResponse;
import ru.java.filesharing.web.dto.file.response.GetFileResponse;

import java.util.List;
import java.util.UUID;

public interface FileService {
    GetFileResponse getById(Long id);

    GetFileResponse getByStorageKey(UUID storageKey);

    List<GetFileResponse> getFilesByUserId(Long userId);

    CreateFileResponse create(Long ownerId, CreateFileRequest request);

    void delete(Long id);
}
