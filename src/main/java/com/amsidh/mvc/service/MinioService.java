package com.amsidh.mvc.service;

import io.minio.GetObjectResponse;
import io.minio.ObjectWriteResponse;
import jdk.jfr.ContentType;

import java.io.InputStream;
import java.nio.file.Path;

public interface MinioService {
    void createBucket(String bucketName) throws Exception;
    boolean isBucketExists(String bucketName) throws Exception;
    ObjectWriteResponse uploadObject(String bucketName, String fileName, Path filePath) throws Exception;
    ObjectWriteResponse putObject(String bucketName, String objectName, InputStream inputStream, String contentType) throws Exception;
    GetObjectResponse getObject(String bucketName, String fileName) throws Exception;
}
