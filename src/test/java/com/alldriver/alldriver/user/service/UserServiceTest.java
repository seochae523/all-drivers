package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.common.emun.Role;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.common.token.dto.AuthToken;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.dto.request.ChangePasswordRequestDto;
import com.alldriver.alldriver.user.dto.request.LoginRequestDto;
import com.alldriver.alldriver.user.dto.request.UserSignUpRequestDto;
import com.alldriver.alldriver.user.dto.request.UserUpdateRequestDto;
import com.alldriver.alldriver.user.dto.response.LoginResponseDto;
import com.alldriver.alldriver.user.dto.response.SignUpResponseDto;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.impl.UserServiceImpl;

import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공(){
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
    @DisplayName("로그인 성공")
    @WithMockUser(username = "testUser", password = "12345678")
    void 로그인_성공(){
        // given

        Optional<User> user = Optional.ofNullable(setUpUser());
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .userId("testUser")
                .password("testPassword")
                .build();

        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();
        List<String> roles = new ArrayList<>();
        Collections.addAll(roles, user.get().getRole());
        Authentication authentication = mock(Authentication.class);
        AuthToken authToken = mock(AuthToken.class);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);

        when(authentication.getPrincipal()).thenReturn(user.get());
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);

        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)){
            when(JwtUtils.generateToken(anyString(), anyList())).thenReturn(authToken);
            // when
            LoginResponseDto response = userService.login(loginRequestDto);

            // then
            assertThat(response.getUserId()).isEqualTo("testUser");
            assertThat(response.getNickname()).isEqualTo("testNickname");
            assertThat(response.getAuthToken()).isInstanceOf(AuthToken.class);

        }


    }


    @Test
    @DisplayName("닉네임 중복 검사 - 중복 있을 때")
    void 닉네임_중복_검사_실패(){
        // given
        when(userRepository.findByNickname("test")).thenThrow(new CustomException(ErrorCode.DUPLICATED_NICKNAME));

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.checkNickname("test"));

        // then
        assertThat(customException.getMessage()).isEqualTo(ErrorCode.DUPLICATED_NICKNAME.getMessage());

    }

    @Test
    @DisplayName("닉네임 중복 검사 - 중복 없을 때")
    void 닉네임_중복_검사_성공(){
        // given
        when(userRepository.findByNickname("newNickname")).thenReturn(Optional.empty());
        // when
        Boolean b = userService.checkNickname("newNickname");

        assertThat(b).isTrue();
    }

    @Test
    @DisplayName("유저 삭제 성공")
    @WithMockUser(username = "testUser", password = "12345678")
    void 삭제_성공(){
        // given
        Optional<User> user = Optional.ofNullable(setUpUser());
        when(userRepository.findByUserId("testUser")).thenReturn(user);
        AuthToken authToken = mock(AuthToken.class);
        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(JwtUtils.getUserId()).thenReturn("testUser");
            // when
            String test = userService.signOut();

            // then
            assertThat(test).isEqualTo("회원 탈퇴 완료.");
        }


    }


    @Test
    @DisplayName("업데이트 성공")
    void 업데이트_성공(){
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
    @DisplayName("비밀번호 변경 성공")
    void 비밀번호_변경_성공(){
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


    @Test
    @DisplayName("로그아웃 성공")
    @WithMockUser(username = "testUser", password = "12345678")
    void 로그아웃_성공(){
        // given
        Optional<User> user = Optional.ofNullable(setUpLogoutUser());
        when(userRepository.findByUserId("testUser")).thenReturn(user);
        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(JwtUtils.getUserId()).thenReturn("testUser");
            // when
            userService.logout();

            // when
            assertThat(user.get().getRefreshToken()).isNull();
        }
    }

    @Test
    @DisplayName("회원 조회 실패")
    void 회원_조회_실패(){
        // given
        ChangePasswordRequestDto build = ChangePasswordRequestDto.builder().userId("wrongUser").password("password").build();
        when(userRepository.findByUserId("wrongUser")).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.changePassword(build));

        // then
        assertThat(customException.getMessage()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());
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

    private User setUpLogoutUser(){
        return User.builder()
                .userId("testUser")
                .name("testName")
                .deleted(false)
                .nickname("testNickname")
                .password("testPassword")
                .role(Role.USER.getValue())
                .phoneNumber("01012345678")
                .refreshToken("testRefreshToken")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
