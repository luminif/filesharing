package ru.java.filesharing.service.impl;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.java.filesharing.config.ApplicationConfig;
import ru.java.filesharing.service.MinioService;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final ApplicationConfig config;
    private final MinioClient minioClient;

    @Override
    @SneakyThrows
    public void upload(String objectName, InputStream inputStream, long size, String contentType) {
        createBucketIfNotExists();

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(config.minio().bucket())
                .object(objectName)
                .stream(inputStream, size, -1)
                .contentType(contentType)
                .build()
        );
    }

    @Override
    @SneakyThrows
    public void delete(String objectName) {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(config.minio().bucket())
                .object(objectName)
                .build()
        );
    }

    @Override
    @SneakyThrows
    public String getPresignedUrl(String objectName) {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(config.minio().bucket())
                .object(objectName)
                .expiry(7, TimeUnit.DAYS)
                .build()
        );
    }

    @SneakyThrows
    private void createBucketIfNotExists() {
        boolean found = minioClient.bucketExists(
            BucketExistsArgs.builder()
                .bucket(config.minio().bucket())
                .build()
        );

        if (!found) {
            minioClient.makeBucket(
                MakeBucketArgs.builder()
                    .bucket(config.minio().bucket())
                    .build()
            );
        }
    }
}
