package com.amsidh.mvc;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import com.amsidh.mvc.service.ObjectStorageService;

import io.minio.ObjectWriteResponse;
import io.minio.UploadObjectArgs;

@SpringBootApplication
public class MinioClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MinioClientApplication.class, args);
	}

	@Autowired
	private ObjectStorageService service;

	@Autowired
	private Environment env;

	@Override
	public void run(String... args) throws Exception {

		//fileStore();

	}

	private void fileStore() throws Exception, IOException {
		final String bucketName = env.getProperty("minio.bucketName");

		if (!service.getBucket(bucketName)) {
			service.createBucket(bucketName);
		} else {
			System.out.println("Bucket " + bucketName + " already exists.");
		}

		final UploadObjectArgs obj = createUploadObjectArgsObj(bucketName);

		final ObjectWriteResponse response = service.uploadObject(obj);

		if (response.etag() != null) {
			System.out.println("file is successfully uploaded !!!!");
		} else {
			System.out.println("file is upload failed !!!!");
		}
	}
	
	private UploadObjectArgs createUploadObjectArgsObj(String bucketName) throws IOException {
		return UploadObjectArgs.builder().bucket(bucketName).object("myPhoto/IMG_20161030_075701.jpg")
				.filename("D:\\photos\\IMG_20161030_075701.jpg").build();
	}
}