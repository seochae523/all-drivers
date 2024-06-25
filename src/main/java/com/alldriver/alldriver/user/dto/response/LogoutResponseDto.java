package com.alldriver.alldriver.user.dto.response;

import com.alldriver.alldriver.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@AllArgsConstructor
@Builder
@Setter
public class LogoutResponseDto {
    @Schema(description = "아이디", example = "123123")
    private String userId;
    @Schema(description = "이름", example = "name")
    private String name;
    @Schema(description = "닉네임", example = "nickname")
    private String nickname;


    public LogoutResponseDto(User user){
        this.userId = user.getUserId();
        this.name = user.getName();
        this.nickname = user.getNickname();
    }
}
