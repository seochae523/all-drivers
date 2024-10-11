package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
public class UserIdFindRequestDto {
    @NotBlank(message = ValidationError.Message.PHONE_NUMBER_NOT_FOUND)
    private String phoneNumber;
}
