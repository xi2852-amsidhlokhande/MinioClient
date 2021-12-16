package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.service.FileService;
import com.amsidh.mvc.service.MinioService;
import io.minio.GetObjectResponse;
import io.minio.ObjectWriteResponse;
import io.minio.Result;
import io.minio.messages.Item;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    MinioService minioService;

    @Value("${file.location}")
    private String fileLocation;

    @Override
    public ResponseEntity uploadFile(String bucketName, MultipartFile file) throws Exception {
        Path filePath = Paths.get(fileLocation + System.currentTimeMillis() + "-" + file.getOriginalFilename());
        Files.write(filePath, file.getBytes());
        ObjectWriteResponse response = minioService.uploadObject(bucketName, file.getOriginalFilename(), filePath);
        return new ResponseEntity("File uploaded Successfully & file name :" + response.object(), HttpStatus.OK);
    }

    @Override
    public ObjectWriteResponse putFile(String bucketName, MultipartFile multipartFile) throws Exception {
        String objectName = System.currentTimeMillis() + "-" + UUID.nameUUIDFromBytes(multipartFile.getBytes()) + multipartFile.getOriginalFilename();
        ObjectWriteResponse objectWriteResponse = minioService.putObject(bucketName, objectName, multipartFile.getInputStream(), multipartFile.getContentType());
        return objectWriteResponse;
    }

    @Override
    public ResponseEntity downloadFile(String bucketName, String fileName) throws Exception {
        GetObjectResponse objectResponse = minioService.getObject(bucketName, fileName);
        byte[] content = IOUtils.toByteArray(objectResponse);
        objectResponse.close();
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(content));
    }

    @Override
    public ResponseEntity totalFilesInBucket(String bucketName) throws Exception {
        Iterable<Result<Item>> objectResponse = minioService.objectsInBucket(bucketName);
        long bucketSize = StreamSupport.stream(objectResponse.spliterator(), false).count();
        return ResponseEntity.ok().body("Total Bucket Size :" + bucketSize);
    }

}
