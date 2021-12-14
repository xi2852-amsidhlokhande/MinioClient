package com.amsidh.mvc.controller;

import com.amsidh.mvc.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@RequiredArgsConstructor
@RestController
@Slf4j
public class FileController {

    private final FileService fileService;
    private final String UPLOAD_FOLDER = "C:/Users/amsid/temp";

    @PostMapping("/uploadFile")
    public ResponseEntity uploadFile(@RequestParam String bucketName, @RequestParam("file") MultipartFile file) {
        log.debug("Called uploadFile method of FileController");
        return fileService.uploadFile(bucketName, file);
        /*saveFile(file);
        return new ResponseEntity("File uploaded Successfully & file name :" + file.getOriginalFilename(), HttpStatus.OK);*/
    }

    @GetMapping("/downloadFile")
    public ResponseEntity downloadFile(@RequestParam String bucketName, @RequestParam String fileName) {
        log.debug("Called downloadFile method of FileController");
        return fileService.downloadFile(bucketName, fileName);
    }

    private void saveFile(MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            String data = new String(bytes);
            Path path = Paths.get(UPLOAD_FOLDER + File.separator + System.currentTimeMillis() + "-" + multipartFile.getOriginalFilename() + File.separator);
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}