package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpRequestDto {
    @Schema(description = "이름", example = "example")
    @NotBlank(message = "User Name Not Found.")
    private String name;

    @Schema(description = "아이디", example = "example")
    @NotBlank(message = "User Id Not Found.")
    private String userId;

    @Schema(description = "비번", example = "example")
    @NotBlank(message = "User Password Not Found.")
    private String password;

    @Schema(description = "별명", example = "example")
    @NotBlank(message = "User Nickname Not Found.")
    private String nickname;

	@Schema(description = "전화번호", example = "01012345678")
    @NotBlank(message = "User Phone Number Not Found.")
    private String phoneNumber;


    public User toEntity(){
        return User.builder()
                .userId(userId)
                .password(password)
                .nickname(nickname)
                .name(name)
                .createdAt(LocalDateTime.now())
                .phoneNumber(phoneNumber)
                .build();
    }
}
