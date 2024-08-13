package com.alldriver.alldriver.common.configuration.chat;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.exception.JwtException;
import com.alldriver.alldriver.common.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Stomp Handler 실행");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        log.info("headers = {}", message.getHeaders());

        log.info("accessor headers = {}", headerAccessor.toNativeHeaderMap());
        log.info("header : {}", headerAccessor.getFirstNativeHeader("Authorization"));


        String fullToken = String.valueOf(headerAccessor.getNativeHeader("Authorization"));

        if(fullToken!=null && fullToken.startsWith("Bearer ")) {
            String token = fullToken.substring(7);
            log.info("token = {}", token);
            JwtUtils.validateToken(token);
        }
        else{
            throw new JwtException(ErrorCode.INVALID_AUTH_TOKEN);
        }

        return message;
    }

    @EventListener
    public void handleWebSocketConnectionListener(SessionConnectedEvent event){
        log.info("사용자 입장");
    }

    @EventListener
    public void handleWebSocketDisconnectionListener(SessionDisconnectEvent event){
        log.info("사용자 퇴장");
    }
}