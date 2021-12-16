package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.service.MinioService;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;


@RequiredArgsConstructor
@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void createBucket(String bucketName) throws Exception {
        log.debug("Called createBucket method of MinioServiceImpl with BucketName {}", bucketName);
        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                .bucket(bucketName)
                .build();
        if (!isBucketExists(bucketName)) minioClient.makeBucket(makeBucketArgs);
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
    public ObjectWriteResponse putObject(String bucketName, String objectName, InputStream inputStream, String contentType) throws Exception {
        createBucket(bucketName);
        PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                inputStream, -1, 10485760)
                .contentType(contentType)
                .build();

        log.info("Called putObject method of MinioServiceImpl {}", putObjectArgs.object());
        log.info("Upload Number :" + atomicInteger.incrementAndGet());
        return minioClient.putObject(putObjectArgs);

    }

    @Override
    public GetObjectResponse getObject(String bucketName, String fileName) throws Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(fileName).build();
        log.debug("Called getObject method of MinioServiceImpl with BucketName {} and FileName {}", bucketName, fileName);
        GetObjectResponse objectResponse = isBucketExists(bucketName) ? minioClient.getObject(getObjectArgs) : null;
        return objectResponse;
    }

    @Override
    public Iterable<Result<Item>> objectsInBucket(String bucketName) throws Exception {
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).build();
        Iterable<Result<Item>> resultIterable = minioClient.listObjects(listObjectsArgs);
        return resultIterable;
    }
}
