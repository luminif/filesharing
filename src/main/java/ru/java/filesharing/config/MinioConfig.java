package ru.java.filesharing.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {
    private final ApplicationConfig config;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(config.minio().url())
            .credentials(
                config.minio().accessKey(),
                config.minio().secretKey()
            )
            .build();
    }
}
