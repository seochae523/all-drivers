package com.alldriver.alldriver.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Builder
public class LicenseValidateRequestDto {
    private String bNo;
}
