package com.amsidh.mvc.controller;

import com.amsidh.mvc.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@RestController
@Slf4j
public class FileController {

    private final FileService fileService;
    private final String UPLOAD_FOLDER = "C:/Users/amsid/temp";
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    @PostMapping("/uploadFile")
    public ResponseEntity uploadFile(@RequestParam String bucketName, @RequestParam("file") MultipartFile file) throws Exception {
        log.debug("Called uploadFile method of FileController");
        return fileService.uploadFile(bucketName, file);
        /*saveFile(file);
        return new ResponseEntity("File uploaded Successfully & file name :" + file.getOriginalFilename(), HttpStatus.OK);*/
    }

    @PostMapping("/putFile")
    public ResponseEntity putFile(@RequestParam String bucketName, @RequestParam("file") MultipartFile multipartFile) throws Exception {
       log.info("\n\n-------------");
        log.info("Record Number :"+ atomicInteger.incrementAndGet());
        log.debug("Called uploadFile method of FileController");
        return fileService.putFile(bucketName, multipartFile);
    }

    @GetMapping("/downloadFile")
    public ResponseEntity downloadFile(@RequestParam String bucketName, @RequestParam String fileName) throws Exception {
        log.debug("Called downloadFile method of FileController");
        return fileService.downloadFile(bucketName, fileName);
    }

    @GetMapping("/fileCount")
    public ResponseEntity bucketFileCount(@RequestParam String bucketName) throws Exception {
        log.debug("Called downloadFile method of FileController");
        return fileService.totalFilesInBucket(bucketName);
    }

    private void saveFile(MultipartFile multipartFile) throws Exception {
        byte[] bytes = multipartFile.getBytes();
        String data = new String(bytes);
        Path path = Paths.get(UPLOAD_FOLDER + File.separator + System.currentTimeMillis() + "-" + multipartFile.getOriginalFilename() + File.separator);
        Files.write(path, bytes);
    }
}