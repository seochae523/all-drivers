package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChangePasswordRequestDto {

    @NotBlank(message = ValidationError.Message.USER_ID_NOT_FOUND)
    private String userId;

    @NotBlank(message = ValidationError.Message.PASSWORD_NOT_FOUND)
    private String password;
}
