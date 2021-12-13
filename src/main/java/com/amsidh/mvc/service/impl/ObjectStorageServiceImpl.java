package com.amsidh.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amsidh.mvc.service.ObjectStorageService;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.UploadObjectArgs;

@Service
public class ObjectStorageServiceImpl implements ObjectStorageService {

	@Autowired
	private MinioClient minioClient;

	@Override
	public void createBucket(String bucketName) throws Exception {
		minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

	}

	@Override
	public boolean getBucket(String bucketName) throws Exception {
		return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
	}

	@Override
	public ObjectWriteResponse uploadObject(UploadObjectArgs arg) throws Exception {
		ObjectWriteResponse response = minioClient.uploadObject(arg);
		return response;
	}

}
