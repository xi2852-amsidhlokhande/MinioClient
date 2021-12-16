package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.domain.BlockReadyMessage;
import com.amsidh.mvc.service.MessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
@Slf4j
public class MessageConsumerImpl implements MessageConsumer {

    @StreamListener("input")
    @Override
    public void consumeMessage(BlockReadyMessage blockReadyMessage) {
        log.info("BlockReady Message is consumed {}", blockReadyMessage);
    }
}
