package ru.java.filesharing.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Security security,
    @NotNull
    Minio minio,
    @NotNull
    String databaseAccessType
) {
    public record Security(
        String secret,
        Long access,
        Long refresh
    ) {
    }

    public record Minio(
        String bucket,
        String url,
        String accessKey,
        String secretKey
    ) {
    }
}
