package com.alldriver.alldriver.common.token.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
public class RefreshRequestDto {
    @Schema(description = "refresh token")
    @NotBlank(message = "User Id Not Found.")
    private String refreshToken;
}
