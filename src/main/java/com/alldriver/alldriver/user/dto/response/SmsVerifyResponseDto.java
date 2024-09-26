package com.alldriver.alldriver.user.dto.response;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SmsVerifyResponseDto {
    private String authCode;
    private String phoneNumber;
    private Boolean authResult;
}
