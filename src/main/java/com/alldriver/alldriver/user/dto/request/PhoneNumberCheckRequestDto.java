package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PhoneNumberCheckRequestDto {


    @NotBlank(message = ValidationError.Message.PHONE_NUMBER_NOT_FOUND)
    private String phoneNumber;
    @Schema(description = "검사 타입 0 = 회원 가입 시 인증 번호 발급 / 1 = 비밀번호 변경 시 계정 확인 / 2 = 잊어버린 비밀번호 변경 시 계정 확인. 이때는 user id 필요", example = "0")
    @NotNull(message = ValidationError.Message.TYPE_NOT_FOUND)
    private Integer type;

    private String userId;
}
