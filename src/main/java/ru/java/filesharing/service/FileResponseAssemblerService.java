package ru.java.filesharing.service;

import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.web.dto.file.response.CreateFileResponse;
import ru.java.filesharing.web.dto.file.response.GetFileResponse;

import java.util.List;

public interface FileResponseAssemblerService {
    GetFileResponse mapToGetFileResponse(File file);

    List<GetFileResponse> mapToGetFileResponse(List<File> files);

    CreateFileResponse mapToCreateFileResponse(File file);
}
