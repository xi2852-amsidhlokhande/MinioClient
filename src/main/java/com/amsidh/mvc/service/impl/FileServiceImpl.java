package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.service.MinioService;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    MinioService minioService;

    @Value("${file.location}")
    private String fileLocation;

    @Override
    public ResponseEntity uploadFile(String bucketName, MultipartFile file) {
        try {
            Path filePath = Paths.get(fileLocation + System.currentTimeMillis() + "-" + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());
            ObjectWriteResponse response = minioService.uploadObject(bucketName, file.getOriginalFilename(), filePath);
            return new ResponseEntity("File uploaded Successfully & file name :" + response.object(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Failed to upload the file", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity putFile(String bucketName, MultipartFile multipartFile) {
        try {
            String objectName = System.currentTimeMillis() + "-" + UUID.nameUUIDFromBytes(multipartFile.getBytes()) + multipartFile.getOriginalFilename();
            ObjectWriteResponse objectWriteResponse = minioService.putObject(bucketName, objectName, multipartFile.getInputStream(), multipartFile.getContentType());
            return new ResponseEntity("File put successfully & file name " + objectWriteResponse.object(), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity("Failed to put the file", HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity downloadFile(String bucketName, String fileName) {

        try {
            GetObjectResponse objectResponse = minioService.getObject(bucketName, fileName);
            byte[] content = IOUtils.toByteArray(objectResponse);
            objectResponse.close();
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(content));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Failed to upload the file", HttpStatus.BAD_REQUEST);
        }
    }


}
