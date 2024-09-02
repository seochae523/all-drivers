package com.alldriver.alldriver.admin.dto.response;


import lombok.*;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCompanyInformationResponseDto {
    private Long id;
    private String licenseNumber;
    private String image;
    private String companyLocation;
    private Date startAt;
}
