package org.example.service;

public interface S3Service {

    String getObjectData(String bucketName, String objectKey);

    void saveContent(String bucketName, String objectKey, String content);
}
