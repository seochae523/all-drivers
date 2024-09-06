package com.alldriver.alldriver.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String userId;
    private String nickname;
    private List<String> roles;
    private AuthToken authToken;
}
