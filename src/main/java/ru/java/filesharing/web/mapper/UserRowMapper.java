package ru.java.filesharing.web.mapper;

import org.springframework.jdbc.core.ResultSetExtractor;
import ru.java.filesharing.entity.user.Role;
import ru.java.filesharing.entity.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserRowMapper implements ResultSetExtractor<User> {
    @Override
    public User extractData(ResultSet rs) throws SQLException {
        User user = null;
        Set<Role> roles = new HashSet<>();

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
        }

        if (user != null) {
            user.setRoles(roles);
        }

        return user;
    }
}
