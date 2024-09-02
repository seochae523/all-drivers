package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.user.domain.CompanyInformation;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
public class CompanyInformationRequestDto {
    @NotBlank(message = ValidationError.Message.LICENSE_NUMBER_NOT_FOUND)
    private String licenseNumber;
    @NotBlank(message = ValidationError.Message.COMPANY_LOCATION_NOT_FOUND)
    private String companyLocation;
    @NotBlank(message = ValidationError.Message.START_AT_NOT_FOUND)
    private Date startAt;

    public CompanyInformation toEntity(String image){
        return CompanyInformation.builder()
                .companyLocation(companyLocation)
                .startAt(startAt)
                .licenseNumber(licenseNumber)
                .image(image)
                .build();
    }
}
