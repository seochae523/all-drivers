package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.user.domain.UserCar;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
public class CarInformationRequestDto {
    @Schema(description = "차 번호")
    @NotBlank(message = "Car Number Not Found.")
    private String carNumber;

    @Schema(description = "차종")
    @NotBlank(message = "Car Category Not Found.")
    private String category;

    @Schema(description = "차 무게")
    @NotBlank(message = "Car Weight Not Found.")
    private String weight;

    public UserCar toEntity(){
        return UserCar.builder()
                .carNumber(carNumber)
                .category(category)
                .weight(weight)

                .build();
    }
}
