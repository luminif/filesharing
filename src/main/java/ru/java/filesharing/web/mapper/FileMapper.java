package ru.java.filesharing.web.mapper;

import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.web.dto.file.response.CreateFileResponse;
import ru.java.filesharing.web.dto.file.response.GetFileResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {
    @Mapping(target = "downloadUrl", expression = "java(downloadUrl)")
    GetFileResponse mapToGetFileResponse(File file, @Context String downloadUrl);

    @IterableMapping(elementTargetType = GetFileResponse.class)
    List<GetFileResponse> mapToGetFileResponse(List<File> files, @Context String downloadUrl);

    @Mapping(target = "downloadUrl", expression = "java(downloadUrl)")
    CreateFileResponse mapToCreateFileResponse(File file, @Context String downloadUrl);
}
