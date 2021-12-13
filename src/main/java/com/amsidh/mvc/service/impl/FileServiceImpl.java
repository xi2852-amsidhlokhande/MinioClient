package com.amsidh.mvc.service.impl;

import io.minio.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amsidh.mvc.service.FileService;
import com.amsidh.mvc.service.ObjectStorageService;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	public MinioClient minioClient;
	@Autowired
	public ObjectStorageService objectStorageService;

	@Value("${file.location}")
	private String fileLocation;

	@Override
	public ResponseEntity uploadFile(String bucketName, MultipartFile file) {
		createBucketNotExists(bucketName);
		try {
			Path filePath = Paths.get(fileLocation + System.currentTimeMillis() + "-" + file.getOriginalFilename());
			Files.write(filePath, file.getBytes());
			ObjectWriteResponse response = minioClient.uploadObject(UploadObjectArgs.builder().bucket(bucketName)
					.object(System.currentTimeMillis() + "-" + file.getOriginalFilename()).filename(filePath.toString())
					.build());
			return new ResponseEntity("File uploaded Successfully & file name :" + response.object(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Failed to upload the file", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity downloadFile(String bucketName, String fileName) {
		createBucketNotExists(bucketName);
		
		try {
			InputStream response = minioClient
					.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
			byte[] content = IOUtils.toByteArray(response);
			response.close();
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
					.body(new ByteArrayResource(content));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Failed to upload the file", HttpStatus.BAD_REQUEST);
		}
	}

	private void createBucketNotExists(String bucketName) {
		try {
			boolean bucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
			if (!bucketExist) {
				objectStorageService.createBucket(bucketName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
