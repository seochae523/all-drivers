package com.alldriver.alldriver.user.service.impl;

import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.user.domain.SmsSession;
import com.alldriver.alldriver.user.dto.request.SmsSendRequestDto;
import com.alldriver.alldriver.user.dto.request.SmsVerifyRequestDto;
import com.alldriver.alldriver.user.dto.response.SmsVerifyResponseDto;
import com.alldriver.alldriver.user.repository.SmsRepository;
import com.alldriver.alldriver.user.service.SmsService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final SmsRepository smsRepository;


    @Value("${sms.api-key}")
    private String apiKey;
    @Value("${sms.secret-key}")
    private String secretKey;
    @Value("${sms.domain}")
    private String domain;
    @Value("${sms.phone-number}")
    private String senderPhoneNumber;


    @Override
    public String sendAuthCode(SmsSendRequestDto smsSendRequestDto) {
        String phoneNumber = smsSendRequestDto.getPhoneNumber();

        if(phoneNumber.length() != 11) {throw new CustomException(ErrorCode.INVALID_PARAMETER, " 전화번호 형식이 일치하지 않습니다.");}
        String code = createCode();

        DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize(apiKey, secretKey, domain);
        Message message = new Message();
        message.setFrom(senderPhoneNumber);
        message.setTo(phoneNumber);
        message.setText("All Drivers 인증번호 ["+code+"]를 화면에 입력해주세요.");

        try {
            messageService.send(message);
            LocalDateTime createdAt = LocalDateTime.now();
            Optional<SmsSession> smsSessionOptional = smsRepository.findByPhoneNumber(phoneNumber);
            if (smsSessionOptional.isPresent()){
                SmsSession smsSession = smsSessionOptional.get();
                smsSession.resetAuthCode(code, createdAt);
                smsRepository.save(smsSession);
            }
            else{
                SmsSession smsSession = SmsSession.builder()
                        .authCode(code)
                        .phoneNumber(phoneNumber)
                        .createdAt(createdAt)
                        .build();
                smsRepository.save(smsSession);
            }


        } catch (NurigoMessageNotReceivedException exception) {
            log.error("{}", exception.getFailedMessageList());
            log.error("{}", exception.getMessage());
            throw new CustomException(ErrorCode.SMS_MESSAGE_NOT_SENT, exception.getMessage());
        } catch (Exception exception) {
            log.error("{}",exception.getMessage());
            throw new CustomException(ErrorCode.INVALID_PARAMETER, exception.getMessage());
        }

        return "문자 발송 완료";
    }

    @Override
    public SmsVerifyResponseDto verifiedCode(SmsVerifyRequestDto smsVerifyRequestDto){
        String authCode = smsVerifyRequestDto.getAuthCode();
        String phoneNumber = smsVerifyRequestDto.getPhoneNumber();


        SmsSession authInfo = smsRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        String AuthCode = authInfo.getAuthCode();

        LocalDateTime createdTime = authInfo.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdTime, now);

        long authCodeExpirationMillis = 1000 * 60 * 3;

        if(duration.toMillis() > authCodeExpirationMillis) throw new CustomException(ErrorCode.SMS_AUTH_CODE_EXPIRED);
        if(!AuthCode.equals(authCode)) throw new CustomException(ErrorCode.INVALID_SMS_AUTH_CODE);

        smsRepository.delete(authInfo);

        return SmsVerifyResponseDto.builder()
                .authResult(true)
                .phoneNumber(phoneNumber)
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
