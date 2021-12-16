package com.amsidh.mvc.service;

import com.amsidh.mvc.domain.BlockReadyMessage;

public interface MessageConsumer {

    void consumeMessage(BlockReadyMessage blockReadyMessage) throws Exception;
}
