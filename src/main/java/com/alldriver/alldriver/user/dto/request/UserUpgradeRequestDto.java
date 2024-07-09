package com.alldriver.alldriver.user.dto.request;


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
    private String carNumber;
    private CarInformationRequestDto carInfo;
    private Integer type;
}
