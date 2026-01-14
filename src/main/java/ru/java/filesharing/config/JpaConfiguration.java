package ru.java.filesharing.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.java.filesharing.repository.FileRepository;
import ru.java.filesharing.repository.UserRepository;
import ru.java.filesharing.repository.jpa.JpaFileRepository;
import ru.java.filesharing.repository.jpa.JpaUserRepository;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaConfiguration {
    @Bean
    public UserRepository userRepository(JpaUserRepository impl) {
        return impl;
    }

    @Bean
    public FileRepository fileRepository(JpaFileRepository impl) {
        return impl;
    }
}