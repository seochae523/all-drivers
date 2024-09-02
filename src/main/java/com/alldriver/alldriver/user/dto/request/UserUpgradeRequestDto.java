package com.alldriver.alldriver.user.dto.request;


import com.alldriver.alldriver.common.enums.ValidationError;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpgradeRequestDto{

    private String licenseNumber;

    private CompanyInformationRequestDto companyInfo;

    private CarInformationRequestDto carInfo;

    @NotNull(message = ValidationError.Message.TYPE_NOT_FOUND)
    private Integer type;
}
