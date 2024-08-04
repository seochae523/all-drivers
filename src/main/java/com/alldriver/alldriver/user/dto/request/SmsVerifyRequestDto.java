package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SmsVerifyRequestDto {

    @NotBlank(message = ValidationError.Message.PHONE_NUMBER_NOT_FOUND)
    private String phoneNumber;

    @NotBlank(message = ValidationError.Message.AUTH_CODE_NOT_FOUND)
    private String authCode;
}
