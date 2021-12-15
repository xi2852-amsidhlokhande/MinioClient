package com.amsidh.mvc.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    ResponseEntity uploadFile(String bucketName, MultipartFile file);

    ResponseEntity putFile(String bucketName, MultipartFile multipartFile);

    ResponseEntity downloadFile(String bucketName, String fileName);
}
