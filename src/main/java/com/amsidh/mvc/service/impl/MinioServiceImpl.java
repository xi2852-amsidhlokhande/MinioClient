package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.service.MinioService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@RequiredArgsConstructor
@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    @Override
    public void createBucket(String bucketName) throws Exception {
        log.debug("Called createBucket method of MinioServiceImpl with BucketName {}", bucketName);
        if (!isBucketExists(bucketName)) minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    @Override
    public boolean isBucketExists(String bucketName) throws Exception {
        log.debug("Called isBucketExists method of MinioServiceImpl with BucketName {}", bucketName);
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    @Override
    public ObjectWriteResponse uploadObject(String bucketName, String fileName, Path filePathContent) throws Exception {
        createBucket(bucketName);
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder().bucket(bucketName)
                .object(System.currentTimeMillis() + "-" + fileName).filename(filePathContent.toString())
                .build();
        log.debug("Called uploadObject method of MinioServiceImpl");
        ObjectWriteResponse response = minioClient.uploadObject(uploadObjectArgs);
        return response;
    }

    @Override
    public GetObjectResponse getObject(String bucketName, String fileName) throws Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(fileName).build();
        log.debug("Called getObject method of MinioServiceImpl with BucketName {} and FileName {}", bucketName, fileName);
        GetObjectResponse objectResponse = isBucketExists(bucketName) ? minioClient.getObject(getObjectArgs) : null;
        return objectResponse;
    }
}
