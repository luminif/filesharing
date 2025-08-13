package ru.java.filesharing.web.dto.file.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateFileRequest(
    @NotNull(message = "File must be not null")
    MultipartFile file,
    @NotNull(message = "Public flag is required")
    Boolean isPublic
) {
}
