package com.alldriver.alldriver.user.service.impl;

import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.user.domain.SmsSession;
import com.alldriver.alldriver.user.dto.request.SmsSendRequestDto;
import com.alldriver.alldriver.user.dto.request.SmsVerifyRequestDto;
import com.alldriver.alldriver.user.dto.response.SmsVerifyResponseDto;
import com.alldriver.alldriver.user.repository.SmsRepository;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final SmsRepository smsRepository;
    private final UserRepository userRepository;
    @Override
    public void sendAuthCode(SmsSendRequestDto smsSendRequestDto) {
        String phoneNumber = smsSendRequestDto.getPhoneNumber();

        String code = createCode();
        DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize("API 키 입력", "API 시크릿 키 입력", "https://api.coolsms.co.kr");
        // Message 패키지가 중복될 경우 net.nurigo.sdk.message.model.Message로 치환하여 주세요
        Message message = new Message();
        message.setFrom("계정에서 등록한 발신번호 입력");
        message.setTo(phoneNumber);
        message.setText("All Driver 인증번호 ["+code+"]를 화면에 입력해주세요.");

        try {
            // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
            messageService.send(message);
            LocalDateTime createdAt = LocalDateTime.now();


            smsRepository.findByPhoneNumber(phoneNumber).ifPresentOrElse(x ->
                    {
                        x.resetAuthCode(code, createdAt);
                        smsRepository.save(x);
                    },
                    () ->
                    {
                        SmsSession smsSession = SmsSession.builder()
                                .authCode(code)
                                .phoneNumber(phoneNumber)
                                .createdAt(createdAt)
                                .build();
                        smsRepository.save(smsSession);
                    });

        } catch (NurigoMessageNotReceivedException exception) {
            // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
            log.error("{}", exception.getFailedMessageList());
            log.error("{}", exception.getMessage());
        } catch (Exception exception) {
            log.error("{}",exception.getMessage());
        }
    }
    private void checkDuplicatedPhoneNumber(String phoneNumber){
        userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER));
    }
    @Override
    public SmsVerifyResponseDto verifiedCode(SmsVerifyRequestDto smsVerifyRequestDto){
        String authCode = smsVerifyRequestDto.getAuthCode();
        String phoneNumber = smsVerifyRequestDto.getPhoneNumber();

        this.checkDuplicatedPhoneNumber(phoneNumber);

        SmsSession authInfo = smsRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        String AuthCode = authInfo.getAuthCode();

        LocalDateTime createdTime = authInfo.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdTime, now);

        long authCodeExpirationMillis = 60*1000*3;

        if(duration.toMillis() > authCodeExpirationMillis) throw new CustomException(ErrorCode.SMS_AUTH_CODE_EXPIRED);
        if(!AuthCode.equals(authCode)) throw new CustomException(ErrorCode.INVALID_SMS_AUTH_CODE);

        smsRepository.delete(authInfo);

        return SmsVerifyResponseDto.builder()
                .authResult(true)
                .phoneNumber(phoneNumber)
                .createdTime(authInfo.getCreatedAt())
                .authCode(authCode)
                .build();
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("userService.createCode() exception occur");
            throw new RuntimeException();
        }
    }
}
