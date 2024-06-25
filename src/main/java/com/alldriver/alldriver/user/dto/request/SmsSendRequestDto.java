package com.alldriver.alldriver.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SmsSendRequestDto {
    @Schema(description = "전화번호", example = "01012345678")
    @NotBlank(message = "User Phone Number Not Found.")
    private String phoneNumber;
}
