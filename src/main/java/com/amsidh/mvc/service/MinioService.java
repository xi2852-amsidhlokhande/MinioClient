package com.amsidh.mvc.service;

import io.minio.GetObjectResponse;
import io.minio.ObjectWriteResponse;

import java.nio.file.Path;

public interface MinioService {
    void createBucket(String bucketName) throws Exception;
    boolean isBucketExists(String bucketName) throws Exception;
    ObjectWriteResponse uploadObject(String bucketName, String fileName, Path filePath) throws Exception;
    GetObjectResponse getObject(String bucketName, String fileName) throws Exception;
}
