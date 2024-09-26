package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.user.domain.FcmToken;
import com.alldriver.alldriver.user.dto.request.FcmSendRequestDto;
import com.alldriver.alldriver.user.repository.FcmTokenRepository;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.impl.FcmServiceImpl;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FcmServiceTest {
    @InjectMocks
    FcmServiceImpl fcmService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FcmTokenRepository fcmTokenRepository;
    @Mock
    private FirebaseMessaging firebaseMessaging;

    @Test
    void sendMessage() {
        //given
        FcmToken token = FcmToken.builder().token("test").build();
        FcmSendRequestDto requestDto = FcmSendRequestDto.builder().title("test").content("test").build();
        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(fcmTokenRepository.findByUserId(any())).thenReturn(Optional.ofNullable(token));
            when(firebaseMessaging.send(any())).thenReturn("");
            //when
            String s = fcmService.sendMessage(requestDto);
            //then
            assertThat(s).isEqualTo("알림 전송 성공");

        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void sendMessageFailWhenFcmTokenNotFound(){
        // given
        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(fcmTokenRepository.findByUserId(any())).thenThrow(new CustomException(ErrorCode.FCM_TOKEN_NOT_FOUND));
            // when
            CustomException exception = assertThrows(CustomException.class, () -> fcmService.sendMessage(new FcmSendRequestDto("test", "test")));
            // then
            Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FCM_TOKEN_NOT_FOUND);
        }
    }


}