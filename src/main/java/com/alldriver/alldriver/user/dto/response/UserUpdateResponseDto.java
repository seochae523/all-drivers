package com.alldriver.alldriver.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserUpdateResponseDto {
    @Schema(description = "아이디", example = "123123")
    private String userId;

}
