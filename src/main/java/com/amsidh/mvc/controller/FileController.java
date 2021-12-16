package com.amsidh.mvc.controller;

import com.amsidh.mvc.domain.BlockReadyMessage;
import com.amsidh.mvc.service.FileService;
import io.minio.ObjectWriteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.amsidh.mvc.common.Constant.MINIO_CROPPED_IMAGE_BUCKET_NAME;
import static com.amsidh.mvc.common.Constant.MINIO_INPUT_IMAGE_BUCKET_NAME;

@EnableBinding(Source.class)
@RequiredArgsConstructor
@RestController
@Slf4j
public class FileController {

    private final FileService fileService;
    private final MessageChannel output;

    private final String UPLOAD_FOLDER = "C:/Users/amsid/temp";
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @PostMapping("/uploadFile")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        log.debug("Called uploadFile method of FileController");
        return fileService.uploadFile(MINIO_INPUT_IMAGE_BUCKET_NAME, file);
        /*saveFile(file);
        return new ResponseEntity("File uploaded Successfully & file name :" + file.getOriginalFilename(), HttpStatus.OK);*/
    }

    @PostMapping("/putFile")
    public ResponseEntity<BlockReadyMessage> putFile(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        log.info("\n\n-------------");
        log.info("Record Number :" + atomicInteger.incrementAndGet());
        log.debug("Called uploadFile method of FileController");
        ObjectWriteResponse objectWriteResponse = fileService.putFile(MINIO_INPUT_IMAGE_BUCKET_NAME, multipartFile);
        BlockReadyMessage blockReadyMessage = BlockReadyMessage.builder()
                .uuid(UUID.nameUUIDFromBytes(multipartFile.getBytes()))
                .bucketName(objectWriteResponse.bucket())
                .fileName(objectWriteResponse.object())
                .contentType(multipartFile.getContentType())
                .build();
        output.send(MessageBuilder.withPayload(blockReadyMessage).build());
        return new ResponseEntity(blockReadyMessage, HttpStatus.ACCEPTED);
    }

    @GetMapping("/downloadFile")
    public ResponseEntity downloadFile(@RequestParam String fileName) throws Exception {
        log.debug("Called downloadFile method of FileController");
        return fileService.downloadFile(MINIO_INPUT_IMAGE_BUCKET_NAME, fileName);
    }

    @GetMapping("/downloadCroppedFile")
    public ResponseEntity downloadCroppedFile(@RequestParam String fileName) throws Exception {
        log.debug("Called downloadFile method of FileController");
        return fileService.downloadFile(MINIO_CROPPED_IMAGE_BUCKET_NAME, fileName);
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