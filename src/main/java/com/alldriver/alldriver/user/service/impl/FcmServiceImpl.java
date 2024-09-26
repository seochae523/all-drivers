package com.alldriver.alldriver.user.service.impl;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.user.domain.FcmToken;
import com.alldriver.alldriver.user.dto.request.FcmSendRequestDto;
import com.alldriver.alldriver.user.repository.FcmTokenRepository;
import com.alldriver.alldriver.user.service.FcmService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FcmServiceImpl implements FcmService {
    private final FirebaseMessaging firebaseMessaging;
    private final FcmTokenRepository fcmTokenRepository;

    @Override
    public String sendMessage(FcmSendRequestDto fcmSendRequestDto) {
        String userId = JwtUtils.getUserId();
        FcmToken fcmToken = fcmTokenRepository
                .findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.FCM_TOKEN_NOT_FOUND));

        String token = fcmToken.getToken();

        Message message = makeMessage(fcmSendRequestDto, token);

        try{
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new CustomException(ErrorCode.FCM_SEND_FAIL, " " + e.getMessage());
        }
        return "알림 전송 성공";
    }

    private Message makeMessage(FcmSendRequestDto fcmSendRequestDto, String token){
        String body = fcmSendRequestDto.getContent();
        String title = fcmSendRequestDto.getTitle();

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        return Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

    }
}
