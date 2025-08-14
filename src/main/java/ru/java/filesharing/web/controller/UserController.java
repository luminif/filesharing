package ru.java.filesharing.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.entity.user.User;
import ru.java.filesharing.service.FileResponseAssemblerService;
import ru.java.filesharing.service.FileService;
import ru.java.filesharing.service.UserService;
import ru.java.filesharing.web.dto.file.request.CreateFileRequest;
import ru.java.filesharing.web.dto.file.response.CreateFileResponse;
import ru.java.filesharing.web.dto.file.response.GetFileResponse;
import ru.java.filesharing.web.dto.user.request.UpdateUserRequest;
import ru.java.filesharing.web.dto.user.response.GetUserResponse;
import ru.java.filesharing.web.dto.user.response.UpdateUserResponse;
import ru.java.filesharing.web.mapper.FileMapper;
import ru.java.filesharing.web.mapper.UserMapper;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@Validated
@Tag(name = "User controller")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FileService fileService;
    private final FileResponseAssemblerService fileResponseAssemblerService;

    private final UserMapper userMapper;
    private final FileMapper fileMapper;

    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    @Operation(summary = "Get user by id")
    public GetUserResponse getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.mapToGetUserResponse(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    @Operation(summary = "Delete user by id")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    @Operation(summary = "Update user's password")
    public UpdateUserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        User user = userMapper.mapFromUpdateUserRequestToEntity(request);
        user.setId(id);
        User updatedUser = userService.update(user);
        return userMapper.mapToUpdateUserResponse(updatedUser);
    }

    @GetMapping("/{id}/files")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    @Operation(summary = "Get files by user id")
    public List<GetFileResponse> getFilesByUserId(@PathVariable Long id) {
        List<File> files = fileService.getFilesByUserId(id);
        return fileResponseAssemblerService.mapToGetFileResponse(files);
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    @Operation(summary = "Create file")
    public CreateFileResponse create(@PathVariable Long id, @Validated @ModelAttribute CreateFileRequest request) {
        File file = fileMapper.mapToEntity(request);
        file.setOwnerId(id);
        File createdFile = fileService.create(file, request.file());
        return fileResponseAssemblerService.mapToCreateFileResponse(fileService.getById(createdFile.getId()));
    }
}
