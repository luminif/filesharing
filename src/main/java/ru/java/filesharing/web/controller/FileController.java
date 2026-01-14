package ru.java.filesharing.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.java.filesharing.service.FileService;
import ru.java.filesharing.web.dto.file.response.GetFileResponse;

@RestController
@RequestMapping("api/v1/files")
@Validated
@Tag(name = "File controller")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/{id}")
    @Operation(summary = "Get file by id")
    public GetFileResponse getById(@PathVariable Long id) {
        return fileService.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessFile(#id)")
    @Operation(summary = "Delete file by id")
    public void deleteById(@PathVariable Long id) {
        fileService.delete(id);
    }
}