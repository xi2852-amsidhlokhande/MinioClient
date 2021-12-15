package com.amsidh.mvc.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ResponseEntity uploadFile(String bucketName, MultipartFile file) throws Exception;

    ResponseEntity putFile(String bucketName, MultipartFile multipartFile) throws Exception;

    ResponseEntity downloadFile(String bucketName, String fileName) throws Exception;

    ResponseEntity totalFilesInBucket(String bucketName) throws Exception;
}
