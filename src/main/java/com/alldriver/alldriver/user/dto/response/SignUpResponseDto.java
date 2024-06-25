package com.alldriver.alldriver.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SignUpResponseDto {
    @Schema(description = "이름", example = "name")
    private String name;
    @Schema(description = "아이디", example = "example")
    private String userId;
    @Schema(description = "닉네임", example = "example")
    private String nickname;

}
