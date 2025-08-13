package ru.java.filesharing.web.dto.file.response;

import java.time.LocalDateTime;

public record GetFileResponse(
    Long id,
    String fileName,
    String downloadUrl,
    Long sizeInBytes,
    String ownerName,
    Boolean isPublic,
    LocalDateTime uploadDate
) {
}
