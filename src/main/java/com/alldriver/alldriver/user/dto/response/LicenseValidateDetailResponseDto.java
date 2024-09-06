package com.alldriver.alldriver.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LicenseValidateDetailResponseDto {
    private String bNo;
    private String bStt;
    private String bSttCd;
    private String taxType;
    private String taxTypeCd;
    private String endDt;
    private String uTccYn;
    private String taxTypeChangeDt;
    private String invoiceApplyAt;
    private String rbfTaxType;
    private String rbfTaxTypeCd;
}
