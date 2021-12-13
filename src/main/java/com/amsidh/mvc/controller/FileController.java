package com.amsidh.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.amsidh.mvc.service.FileService;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/uploadFile")
    public ResponseEntity uploadFile(@RequestParam String bucketName, @RequestBody MultipartFile file) {
        return fileService.uploadFile(bucketName, file);
    }

    @GetMapping("/downloadFile")
    public ResponseEntity downloadFile(@RequestParam String bucketName, @RequestParam String fileName) {
        return fileService.downloadFile(bucketName, fileName);
    }
}