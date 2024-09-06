package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
public class CarInformationRequestDto {
    @NotBlank(message = ValidationError.Message.CAR_NUMBER_NOT_FOUND)
    private String carNumber;

    @NotBlank(message = ValidationError.Message.CATEGORY_NOT_FOUND)
    private String category;

    @NotBlank(message = ValidationError.Message.CAR_WEIGHT_NOT_FOUND)
    private String weight;


}
