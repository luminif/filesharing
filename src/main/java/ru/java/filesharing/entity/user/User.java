package ru.java.filesharing.entity.user;

import lombok.Getter;
import lombok.Setter;
import ru.java.filesharing.entity.file.File;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class User {
    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;
    private List<File> files;
    private LocalDateTime createdAt;
}