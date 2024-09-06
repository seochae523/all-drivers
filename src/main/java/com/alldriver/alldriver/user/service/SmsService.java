package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.user.dto.request.SmsSendRequestDto;
import com.alldriver.alldriver.user.dto.request.SmsVerifyRequestDto;
import com.alldriver.alldriver.user.dto.response.SmsVerifyResponseDto;

public interface SmsService {

    String sendAuthCode(SmsSendRequestDto smsSendRequestDto);
    SmsVerifyResponseDto verifiedCode(SmsVerifyRequestDto smsVerifyRequestDto);
}
