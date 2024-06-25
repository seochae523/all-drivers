package com.alldriver.alldriver.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChangePasswordRequestDto {
    @Schema(description = "아이디", example = "example")
    @NotBlank(message = "User Id Not Found.")
    private String userId;
    @Schema(description = "바꿀 비밀번호", example = "example")
    @NotBlank(message = "User Password Not Found.")
    private String password;
}
