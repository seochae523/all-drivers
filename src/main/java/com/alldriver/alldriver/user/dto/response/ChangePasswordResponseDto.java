package com.alldriver.alldriver.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class ChangePasswordResponseDto {
    private String userId;
    private String nickname;
}
