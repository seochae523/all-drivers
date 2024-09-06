package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Builder
public class LicenseValidateRequestDto {
    @NotBlank(message = ValidationError.Message.BUSINESS_NUMBER_NOT_FOUND)
    private String bNo;
}
