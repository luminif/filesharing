package ru.java.filesharing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.service.FileResponseAssemblerService;
import ru.java.filesharing.service.MinioService;
import ru.java.filesharing.web.dto.file.response.CreateFileResponse;
import ru.java.filesharing.web.dto.file.response.GetFileResponse;
import ru.java.filesharing.web.mapper.FileMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileResponseAssemblerServiceImpl implements FileResponseAssemblerService {
    private final FileMapper fileMapper;
    private final MinioService minioService;

    @Override
    public GetFileResponse mapToGetFileResponse(File file) {
        String downloadUrl = minioService.getPresignedUrl(file.getStorageKey());
        return fileMapper.mapToGetFileResponse(file, downloadUrl);
    }

    @Override
    public List<GetFileResponse> mapToGetFileResponse(List<File> files) {
        return files.stream()
            .map(this::mapToGetFileResponse)
            .toList();
    }

    @Override
    public CreateFileResponse mapToCreateFileResponse(File file) {
        String downloadUrl = minioService.getPresignedUrl(file.getStorageKey());
        return fileMapper.mapToCreateFileResponse(file, downloadUrl);
    }
}
