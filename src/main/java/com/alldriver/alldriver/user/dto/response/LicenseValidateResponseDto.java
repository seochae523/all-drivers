package com.alldriver.alldriver.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LicenseValidateResponseDto {
    private String statusCode;
    private Integer matchCnt;
    private Integer requestCnt;
    private LicenseValidateDetailResponseDto data;
}
