package com.alldriver.alldriver.user.dto.response;

import com.alldriver.alldriver.common.token.dto.AuthToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String userId;
    private String nickname;
    private AuthToken authToken;
}
