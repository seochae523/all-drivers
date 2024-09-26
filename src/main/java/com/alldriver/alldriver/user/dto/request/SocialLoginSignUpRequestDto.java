package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SocialLoginSignUpRequestDto {
    @NotBlank(message = ValidationError.Message.USER_ID_NOT_FOUND)
    private String userId;

    @NotBlank(message = ValidationError.Message.FCM_TOKEN_NOT_FOUND)
    private String fcmToken;
}
