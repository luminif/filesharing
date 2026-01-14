package ru.java.filesharing.entity.file;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Getter
@Setter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "storage_key")
    private String storageKey;

    @Column(name = "size_bytes")
    private Long sizeInBytes;

    @Column(name = "owner_id")
    private Long ownerId;

    @Transient
    private String ownerName;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @PrePersist
    protected void onCreate() {
        if (this.uploadDate == null) {
            this.uploadDate = LocalDateTime.now();
        }
    }
}