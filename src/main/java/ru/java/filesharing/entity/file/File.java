package ru.java.filesharing.entity.file;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class File {
    private Long id;
    private String fileName;
    private String storageKey;
    private Long sizeInBytes;
    private Long ownerId;
    private String ownerName;
    private Boolean isPublic;
    private LocalDateTime uploadDate;
}