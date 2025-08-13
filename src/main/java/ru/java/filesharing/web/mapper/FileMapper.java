package ru.java.filesharing.web.mapper;

import org.mapstruct.Mapper;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.web.dto.file.request.CreateFileRequest;
import ru.java.filesharing.web.dto.file.response.CreateFileResponse;
import ru.java.filesharing.web.dto.file.response.GetFileResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {
    GetFileResponse mapToGetFileResponse(File file);

    List<GetFileResponse> mapToGetFileResponse(List<File> files);

    CreateFileResponse mapToCreateFileResponse(File file);

    File mapToEntity(CreateFileRequest request);
}
