package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
public class UserUpdateRequestDto {

    @NotBlank(message = ValidationError.Message.USER_ID_NOT_FOUND)
    private String userId;


}
