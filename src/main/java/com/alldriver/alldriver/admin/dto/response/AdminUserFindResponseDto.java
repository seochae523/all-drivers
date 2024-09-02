package com.alldriver.alldriver.admin.dto.response;


import com.alldriver.alldriver.user.domain.FcmToken;
import com.alldriver.alldriver.user.domain.UserCarInformation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserFindResponseDto {
    private Long id;
    private String userId;
    private String name;
    private String phoneNumber;
    private Boolean deleted;
    private String nickname;
    private String refreshToken;
    private LocalDateTime createdAt;
    private String role;
    private List<AdminCompanyInformationResponseDto> companyInformation;
    private List<AdminUserCarInformationResponseDto> userCarInformation;
//    private FcmToken fcmToken;
}
