package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.user.domain.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarOwnerSignUpRequestDto {
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

    @Schema(description = "차 정보")
    private CarInformationRequestDto carInformation;

    @Schema(description = "fcm 토큰", example = "example")
    @NotBlank(message = "User Fcm Token Not Found.")
    private String fcmToken;

    public User toEntity(){
        return User.builder()
                .userId(userId)
                .name(name)
                .password(password)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
