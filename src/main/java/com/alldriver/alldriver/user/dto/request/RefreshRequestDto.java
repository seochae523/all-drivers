package com.alldriver.alldriver.user.dto.request;


import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RefreshRequestDto {

    @NotBlank(message = ValidationError.Message.REFRESH_TOKEN_NOT_FOUND)
    private String refreshToken;
}
