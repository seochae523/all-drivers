package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.user.dto.request.PhoneNumberCheckRequestDto;
import com.alldriver.alldriver.user.dto.request.SmsSendRequestDto;
import com.alldriver.alldriver.user.dto.request.SmsVerifyRequestDto;
import com.alldriver.alldriver.user.dto.response.SmsVerifyResponseDto;
import com.alldriver.alldriver.user.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "sms")
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/sms/send")
    @Operation(description = "sms 문자 인증 발송")
    public ResponseEntity<String> sendSmsAuthCode(@RequestBody @Valid SmsSendRequestDto smsSendRequestDto){
        return ResponseEntity.ok(smsService.sendAuthCode(smsSendRequestDto));
    }
    @PostMapping("/sms/verify")
    @Operation(description = "sms 문자 인증 검증")
    public ResponseEntity<SmsVerifyResponseDto> sendSmsAuthCode(@RequestBody @Valid SmsVerifyRequestDto smsVerifyRequestDto){
        return ResponseEntity.ok(smsService.verifiedCode(smsVerifyRequestDto));
    }


}
