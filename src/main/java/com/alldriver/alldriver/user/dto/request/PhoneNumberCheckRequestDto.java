package com.alldriver.alldriver.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class PhoneNumberCheckRequestDto {

    @Schema(description = "전화번호", example = "01012345678")
    @NotBlank(message = "User Phone Number Not Found.")
    private String phoneNumber;
    @Schema(description = "검사 타입 0 = 회원 가입 시 인증 번호 발급 / 1 = 비밀번호 변경 시 계정 확인", example = "0")
    @NotNull(message = "Type Not Found.")
    private Integer type;

}
