package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.domain.BlockReadyMessage;
import com.amsidh.mvc.service.MessageConsumer;
import com.amsidh.mvc.service.MinioService;
import io.minio.GetObjectResponse;
import io.minio.ObjectWriteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import java.io.*;

import static com.amsidh.mvc.common.Constant.MINIO_CROPPED_IMAGE_BUCKET_NAME;
import static com.amsidh.mvc.common.Constant.MINIO_INPUT_IMAGE_BUCKET_NAME;

@EnableBinding(Sink.class)
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageConsumerImpl implements MessageConsumer {

    private final MinioService minioService;
    private final static String TEMP_DIR = "C:/Users/amsidh.lokhande/tmp";

    @StreamListener("input")
    @Override
    public void consumeMessage(BlockReadyMessage blockReadyMessage) throws Exception {
        log.info("BlockReady Message is consumed {}", blockReadyMessage);
        //Read file from Minio
        GetObjectResponse getObjectResponse = minioService.getObject(MINIO_INPUT_IMAGE_BUCKET_NAME,blockReadyMessage.getFileName());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = getObjectResponse.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        byte[] bytes = buffer.toByteArray();

        // Call the detectSignature api and get Cropped Image as byte array
        //Save the byte array image to minio

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        ObjectWriteResponse objectWriteResponse = minioService.putObject(MINIO_CROPPED_IMAGE_BUCKET_NAME, blockReadyMessage.getUuid() + "-cropped-" + blockReadyMessage.getFileName(), byteArrayInputStream, blockReadyMessage.getContentType());
        log.info("Cropped Image saved to Minio with name {}", objectWriteResponse.object());
    }
}
