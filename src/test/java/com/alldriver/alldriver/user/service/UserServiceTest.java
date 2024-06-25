package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.common.emun.Role;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.common.token.AuthTokenProvider;
import com.alldriver.alldriver.common.token.dto.AuthToken;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.dto.request.ChangePasswordRequestDto;
import com.alldriver.alldriver.user.dto.request.LoginRequestDto;
import com.alldriver.alldriver.user.dto.request.UserSignUpRequestDto;
import com.alldriver.alldriver.user.dto.request.UserUpdateRequestDto;
import com.alldriver.alldriver.user.dto.response.DeleteResponseDto;
import com.alldriver.alldriver.user.dto.response.LoginResponseDto;
import com.alldriver.alldriver.user.dto.response.SignUpResponseDto;
import com.alldriver.alldriver.user.repository.UserCarRepository;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthTokenProvider authTokenProvider;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserCarRepository userCarRepository;


    @BeforeEach
    void init(){
        userService = new UserServiceImpl(userRepository, userCarRepository, passwordEncoder, authTokenProvider, authenticationManagerBuilder);
    }

    @Test
    @DisplayName("회원가입")
    void 회원가입(){
        //given

        UserSignUpRequestDto request = setUpSignUpRequestDto();
        when(userRepository.save(any())).thenReturn(request.toEntity());
        //when
        SignUpResponseDto response = userService.signUpUser(request);

        //then

        assertThat(request.getName()).isEqualTo(response.getName());
        assertThat(request.getNickname()).isEqualTo(response.getNickname());
        assertThat(request.getUserId()).isEqualTo(response.getUserId());
    }


    @Test
    @DisplayName("로그인_성공")
    void 로그인_성공(){
        // given

        Optional<User> user = Optional.ofNullable(setUpUser());
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .userId("testUser")
                .password("testPassword")
                .build();

        when(userRepository.findByUserId("testUser")).thenReturn(user);

        Authentication authentication = mock(Authentication.class);
        AuthToken authToken = mock(AuthToken.class);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authTokenProvider.generateToken(any(), any())).thenReturn(authToken);

        // when
        LoginResponseDto response = userService.login(loginRequestDto);

        // then
        assertThat(response.getUserId()).isEqualTo("testUser");
        assertThat(response.getNickname()).isEqualTo("testNickname");
        assertThat(response.getAuthToken()).isInstanceOf(AuthToken.class);
    }



    @Test
    @DisplayName("닉네임 중복 검사")
    void 닉네임_중복_검사(){
        // given
        when(userRepository.findByNickname("test")).thenThrow(new CustomException(ErrorCode.DUPLICATED_NICKNAME));

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.checkNickname("test"));

        // then
        assertThat(customException.getMessage()).isEqualTo(ErrorCode.DUPLICATED_NICKNAME.getMessage());

    }
    @Test
    @DisplayName("유저 삭제")
    void 삭제(){
        // given
        Optional<User> user = Optional.ofNullable(setUpUser());
        when(userRepository.findByUserId("testUser")).thenReturn(user);

        // when
        DeleteResponseDto test = userService.delete("testUser");

        // then
        assertThat(test.getUserId()).isEqualTo(user.get().getUserId());
        assertThat(test.getName()).isEqualTo(user.get().getName());
        assertThat(user.get().getDeleted()).isEqualTo(true);
    }

    @Test
    @DisplayName("업데이트")
    void 업데이트(){
        // given
        Optional<User> user = Optional.ofNullable(setUpUser());
        UserUpdateRequestDto userUpdateRequestDto = setUpUserUpdateResponseDto();
        when(userRepository.findByUserId("testUser")).thenReturn(user);

        // when
        userService.update(userUpdateRequestDto);

        // then
        assertThat(user.get().getNickname()).isEqualTo(userUpdateRequestDto.getNickname());
    }


    @Test
    @DisplayName("비밀번호 변경")
    void 비밀번호_변경(){
        // given
        Optional<User> user = Optional.ofNullable(setUpUser());
        ChangePasswordRequestDto changePasswordRequestDto = setUpChangePasswordRequestDto();
        when(userRepository.findByUserId("testUser")).thenReturn(user);
        when(passwordEncoder.encode(changePasswordRequestDto.getPassword())).thenReturn("encodedChangedPassword");

        // when
        userService.changePassword(changePasswordRequestDto);

        // then
        assertThat(user.get().getPassword()).isEqualTo("encodedChangedPassword");
    }


    private UserUpdateRequestDto setUpUserUpdateResponseDto(){
        return UserUpdateRequestDto.builder()
                .userId("testUser")
                .nickname("changedUserNickname")
                .build();
    }

    private ChangePasswordRequestDto setUpChangePasswordRequestDto(){
        return ChangePasswordRequestDto.builder()
                .userId("testUser")
                .password("changedPassword")
                .build();
    }

    private UserSignUpRequestDto setUpSignUpRequestDto(){
        return UserSignUpRequestDto.builder()
                .name("testName")
                .userId("testUser")
                .phoneNumber("01012345678")
                .password("testPassword")
                .nickname("testNickname")
                .build();
    }

    private User setUpUser(){
        return User.builder()
                .userId("testUser")
                .name("testName")
                .deleted(false)
                .nickname("testNickname")
                .password("testPassword")
                .role(Role.USER.getValue())
                .phoneNumber("01012345678")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
