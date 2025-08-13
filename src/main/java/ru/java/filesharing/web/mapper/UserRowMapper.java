package ru.java.filesharing.web.mapper;

import org.springframework.jdbc.core.ResultSetExtractor;
import ru.java.filesharing.entity.file.File;
import ru.java.filesharing.entity.user.Role;
import ru.java.filesharing.entity.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRowMapper implements ResultSetExtractor<User> {
    @Override
    public User extractData(ResultSet rs) throws SQLException {
        User user = null;
        Set<Role> roles = new HashSet<>();
        List<File> files = new ArrayList<>();

        while (rs.next()) {
            if (user == null) {
                user = new User();
                user.setId(rs.getLong("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
            }

            String roleStr = rs.getString("user_role");
            if (roleStr != null) {
                roles.add(Role.valueOf(roleStr));
            }

            Long fileId = rs.getObject("file_id", Long.class);
            if (fileId != null) {
                File file = new File();
                file.setId(fileId);
                file.setFileName(rs.getString("filename"));
                file.setStorageKey(rs.getString("storage_key"));
                file.setSizeInBytes(rs.getLong("size_bytes"));
                file.setUploadDate(rs.getObject("upload_date", LocalDateTime.class));
                file.setIsPublic(rs.getBoolean("is_public"));
                file.setOwnerId(rs.getLong("owner_id"));
                file.setOwnerName(rs.getString("owner_name"));
                files.add(file);
            }
        }

        if (user != null) {
            user.setRoles(roles);
            user.setFiles(files);
        }

        return user;
    }
}
