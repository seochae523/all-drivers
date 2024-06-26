package com.alldriver.alldriver.common.token.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
public class RefreshRequestDto {
    @Schema(description = "아이디", example = "example")
    @NotBlank(message = "User Id Not Found.")
    private String userId;

    @Schema(description = "access token")
    @NotBlank(message = "User Id Not Found.")
    private String accessToken;

    @Schema(description = "refresh token")
    @NotBlank(message = "User Id Not Found.")
    private String refreshToken;
}
