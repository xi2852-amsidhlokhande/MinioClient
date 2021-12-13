package com.amsidh.mvc.service;

import io.minio.ObjectWriteResponse;
import io.minio.UploadObjectArgs;

public interface ObjectStorageService {
	void createBucket(String bucketName) throws Exception;

	boolean getBucket(String bucketName) throws Exception;

	ObjectWriteResponse uploadObject(UploadObjectArgs arg) throws Exception;
	
}
