package com.amsidh.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.minio.MinioClient;

@Configuration
public class MinioConfig {

	@Autowired
	private Environment environment;

	@Bean
	public MinioClient getClient() {
		return MinioClient.builder().endpoint(environment.getProperty("minio.url"))
				.credentials(environment.getProperty("minio.accessKey"), environment.getProperty("minio.secretKey"))
				.build();
	}

}