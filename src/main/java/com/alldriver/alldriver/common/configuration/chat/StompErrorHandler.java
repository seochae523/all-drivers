package com.alldriver.alldriver.common.configuration.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StompErrorHandler extends StompSubProtocolErrorHandler {
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {

        return sendErrorMessage(ex.getLocalizedMessage());
    }



    private Message<byte[]> sendErrorMessage(String errorMessage) {
        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.ERROR);
        headers.setMessage(errorMessage);
        headers.setLeaveMutable(true);

        return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8),
                    headers.getMessageHeaders());

    }
}
