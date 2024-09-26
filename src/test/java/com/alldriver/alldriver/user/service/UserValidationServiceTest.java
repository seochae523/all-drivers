package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.LicenseNumberValidator;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.dto.request.PhoneNumberCheckRequestDto;
import com.alldriver.alldriver.user.repository.LicenseRepository;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.impl.UserValidationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {
    @InjectMocks
    UserValidationServiceImpl userValidationService;

    @Mock
    UserRepository userRepository;

    @Mock
    LicenseRepository licenseRepository;

    @Mock
    LicenseNumberValidator licenseNumberValidator;
    private final String userId = "testUser";
    private final String license = "testLicense";
    private final String phoneNumber = "01012345678";
    private final String nickname = "testNick";

    @Test
    @DisplayName("중복 회원 id 탐지 - 중복 있을 때")
    void checkDuplicatedUserId() {
        // given
        when(userRepository.findByUserId(any())).thenThrow(new CustomException(ErrorCode.DUPLICATED_ACCOUNT, " User Id = " + userId));
        // when
        CustomException customException = assertThrows(CustomException.class, () -> userValidationService.checkDuplicatedAccount(userId));

        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_ACCOUNT);
        assertThat(customException.getMessage()).isEqualTo(ErrorCode.DUPLICATED_ACCOUNT.getMessage() + " User Id = " + userId);

    }
    @Test
    @DisplayName("중복 회원 id 탐지 - 중복 없을 때")
    void checkUnDuplicatedUserId() {
        // given
        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());
        // when
        Boolean b = userValidationService.checkDuplicatedAccount(userId);
        // then
        assertThat(b).isTrue();
    }

    @Test
    @DisplayName("중복 사업자 번호 탐지 - 중복 있을 때")
    void checkDuplicatedLicense() {
        // given
        when(licenseRepository.findByLicenseNumber(any())).thenThrow(new CustomException(ErrorCode.DUPLICATED_LICENSE_NUMBER, " License Number = " + license));
        // when
        CustomException customException = assertThrows(CustomException.class, () -> userValidationService.checkLicense(license));

        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_LICENSE_NUMBER);
        assertThat(customException.getMessage()).isEqualTo(ErrorCode.DUPLICATED_LICENSE_NUMBER.getMessage() + " License Number = " + license);
    }
    @Test
    @DisplayName("중복 사업자 번호 탐지 - 중복 없을 때")
    void checkUnDuplicatedLicense() throws URISyntaxException {
        // given
        when(licenseRepository.findByLicenseNumber(any())).thenReturn(Optional.empty());
        when(licenseNumberValidator.validateLicense(any())).thenReturn(true);
        // when
        Boolean b = userValidationService.checkLicense(license);
        // then
        assertThat(b).isTrue();
    }


    @Test
    @DisplayName("회원 가입 전화 번호 검증 - 중복 없을 때")
    void checkUnDuplicatedPhoneNumberWhenSignUp() {
        // given
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = setUpPhoneNumberCheckRequest(phoneNumber, 0);
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());
        // when
        Boolean b = userValidationService.checkPhoneNumber(phoneNumberCheckRequestDto);
        // then
        assertThat(b).isTrue();
    }
    @Test
    @DisplayName("회원 가입 전화 번호 검증 - 중복 있을 때")
    void checkDuplicatedPhoneNumberWhenSignUp() {
        // given
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = setUpPhoneNumberCheckRequest(phoneNumber, 0);
        when(userRepository.findByPhoneNumber(any())).thenThrow(new CustomException(ErrorCode.DUPLICATED_ACCOUNT));
        // when
        CustomException customException = assertThrows(CustomException.class, () -> userValidationService.checkPhoneNumber(phoneNumberCheckRequestDto));

        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_ACCOUNT);
        assertThat(customException.getMessage()).isEqualTo(ErrorCode.DUPLICATED_ACCOUNT.getMessage());
    }

    @Test
    @DisplayName("비밀번호 변경 전화 번호 검증 - 사용자 있을 때")
    void checkUnDuplicatedPhoneNumberWhenChangePassword() {
        // given
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = setUpPhoneNumberCheckRequest(phoneNumber, 1);
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(new User()));
        // when
        Boolean b = userValidationService.checkPhoneNumber(phoneNumberCheckRequestDto);
        // then
        assertThat(b).isTrue();
    }

    @Test
    @DisplayName("비밀번호 변경 전화 번호 검증 - 사용자 없을 때")
    void checkDuplicatedPhoneNumberWhenChangePassword() {
        // given
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = setUpPhoneNumberCheckRequest(phoneNumber, 1);
        when(userRepository.findByPhoneNumber(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        // when
        CustomException customException = assertThrows(CustomException.class, () -> userValidationService.checkPhoneNumber(phoneNumberCheckRequestDto));

        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND);
        assertThat(customException.getMessage()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("잊어버린 비밀번호 변경 전화 번호 검증 - 사용자 있을 때")
    void checkUnDuplicatedPhoneNumberWhenChangeForgetPassword() {
        // given
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = setUpPhoneNumberCheckRequest(phoneNumber, 2);
        when(userRepository.findByUserIdAndPhoneNumber(any(), any())).thenReturn(Optional.of(new User()));
        // when
        Boolean b = userValidationService.checkPhoneNumber(phoneNumberCheckRequestDto);
        // then
        assertThat(b).isTrue();
    }

    @Test
    @DisplayName("잊어버린 비밀번호 변경 전화 번호 검증 - 사용자 없을 때")
    void checkDuplicatedPhoneNumberWhenChangeForgetPassword() {
        // given
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = setUpPhoneNumberCheckRequest(phoneNumber, 2);
        when(userRepository.findByUserIdAndPhoneNumber(any(), any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        // when
        CustomException customException = assertThrows(CustomException.class, () -> userValidationService.checkPhoneNumber(phoneNumberCheckRequestDto));

        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND);
        assertThat(customException.getMessage()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());
    }
    @Test
    @DisplayName("전화 번호 검증 - 타입 파라미터 에러")
    void checkDuplicatedPhoneNumberWhenTypeHasError() {
        // given
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = setUpPhoneNumberCheckRequest(phoneNumber, -1);
        // when
        CustomException customException = assertThrows(CustomException.class, () -> userValidationService.checkPhoneNumber(phoneNumberCheckRequestDto));
        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INVALID_PARAMETER);
        assertThat(customException.getMessage()).isEqualTo(ErrorCode.INVALID_PARAMETER.getMessage()+" type 파라미터는 0 ~ 2이어야 합니다.");
    }

    private PhoneNumberCheckRequestDto setUpPhoneNumberCheckRequest(String phoneNumber, Integer type){
        return PhoneNumberCheckRequestDto.builder()
                .phoneNumber(phoneNumber)
                .type(type)
                .build();
    }

}