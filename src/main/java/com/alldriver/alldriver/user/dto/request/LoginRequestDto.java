package com.alldriver.alldriver.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @Schema(description = "아이디", example = "example")
    @NotBlank(message = "User Id Not Found.")
    private String userId;

    @Schema(description = "비밀번호", example = "example")
    @NotBlank(message = "User Password Not Found.")
    private String password;

    @Schema(description = "fcm 토큰", example = "example")
    @NotBlank(message = "User Fcm Token Not Found.")
    private String fcmToken;

}
