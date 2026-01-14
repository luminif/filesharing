package ru.java.filesharing.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.java.filesharing.repository.FileRepository;
import ru.java.filesharing.repository.UserRepository;
import ru.java.filesharing.repository.jdbc.JdbcFileRepository;
import ru.java.filesharing.repository.jdbc.JdbcUserRepository;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfiguration {
    @Bean
    public UserRepository userRepository(JdbcUserRepository impl) {
        return impl;
    }

    @Bean
    public FileRepository fileRepository(JdbcFileRepository impl) {
        return impl;
    }
}
