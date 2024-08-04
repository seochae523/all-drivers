package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.user.domain.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = ValidationError.Message.NAME_NOT_FOUND)
    private String name;

    @NotBlank(message = ValidationError.Message.USER_ID_NOT_FOUND)
    private String userId;

    @NotBlank(message = ValidationError.Message.PASSWORD_NOT_FOUND)
    private String password;

    @NotBlank(message = ValidationError.Message.NICKNAME_NOT_FOUND)
    private String nickname;

    @NotBlank(message = ValidationError.Message.PHONE_NUMBER_NOT_FOUND)
    private String phoneNumber;

    @NotBlank(message = ValidationError.Message.FCM_TOKEN_NOT_FOUND)
    private String fcmToken;

    @NotNull(message = ValidationError.Message.CAR_INFORMATION_NOT_FOUND)
    private CarInformationRequestDto carInformation;


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
