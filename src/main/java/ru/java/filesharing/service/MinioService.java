package ru.java.filesharing.service;

import java.io.InputStream;

public interface MinioService {
    void upload(String objectName, InputStream inputStream, long size, String contentType);

    void delete(String objectName);

    String getPresignedUrl(String objectName);
}
