package ru.java.filesharing.web.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.java.filesharing.entity.file.File;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FileRowMapper implements RowMapper<File> {
    @Override
    public File mapRow(ResultSet rs, int rowNum) throws SQLException {
        File file = new File();
        file.setId(rs.getLong("file_id"));
        file.setFileName(rs.getString("filename"));
        file.setStorageKey(rs.getString("storage_key"));
        file.setSizeInBytes(rs.getLong("size_bytes"));
        file.setOwnerId(rs.getLong("owner_id"));
        file.setOwnerName(rs.getString("owner_name"));
        file.setUploadDate(rs.getObject("upload_date", LocalDateTime.class));
        file.setIsPublic(rs.getBoolean("is_public"));
        return file;
    }
}
