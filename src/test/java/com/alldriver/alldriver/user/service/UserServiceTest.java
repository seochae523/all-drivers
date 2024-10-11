package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.board.domain.Car;
import com.alldriver.alldriver.board.dto.response.CarFindResponseDto;
import com.alldriver.alldriver.board.repository.CarRepository;
import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.user.dto.request.*;
import com.alldriver.alldriver.user.dto.response.AuthToken;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.dto.response.LoginResponseDto;
import com.alldriver.alldriver.user.dto.response.SignUpResponseDto;
import com.alldriver.alldriver.user.dto.response.UserIdFindResponseDto;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.impl.UserServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


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
    private CarRepository carRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("로그인 성공")
    void login(){
        // given
        // user 설정
        Optional<User> user = Optional.ofNullable(setUpUser());

        // login request 설정
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .userId("testUser")
                .password("testPassword")
                .build();
        List<String> roles = new ArrayList<>();
        Collections.addAll(roles, user.get().getRole());

        // auth 관련 mocking
        Authentication authentication = mock(Authentication.class);
        AuthToken authToken = mock(AuthToken.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUserId(), loginRequestDto.getPassword());

        when(authentication.getPrincipal()).thenReturn(user.get());
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(authenticationToken)).thenReturn(authentication);


        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)){
            when(JwtUtils.generateToken(anyString(), anyList())).thenReturn(authToken);
            // when
            LoginResponseDto response = userService.login(loginRequestDto);

            // then
            assertThat(response.getUserId()).isEqualTo("testUser");
            assertThat(response.getAuthToken()).isInstanceOf(AuthToken.class);
        }
    }
    @Test
    @DisplayName("회원가입 성공 - 유저")
    void 회원가입_성공(){
        //given
        UserSignUpRequestDto request = setUpSignUpRequestDto();
        User user = User.builder()
                .name("test")
                .userId("test")
                .password("test")
                .phoneNumber("test")
                .role(Role.USER.getValue())
                .build();
        when(userRepository.save(any())).thenReturn(user);
        //when
        SignUpResponseDto response = userService.signUpUser(request);

        //then
        assertThat(request.getName()).isEqualTo(response.getName());
        assertThat(request.getUserId()).isEqualTo(response.getUserId());
        assertThat(response.getRole()).isEqualTo(Role.USER.getValue());
    }

    @Test
    @DisplayName("회원가입 성공 - 차주 - 구직자")
    void signUpSuccessCarOwnerJobSeeker() throws IOException {
        // given
        CarOwnerSignUpRequestDto carOwnerSignUpRequestDto = CarOwnerSignUpRequestDto.builder()
                .name("test")
                .carId(1L)
                .type(0)
                .userId("test")
                .fcmToken("test")
                .password("test")
                .phoneNumber("test")
                .build();
        User user = User.builder()
                .name("test")
                .userId("test")
                .password("test")
                .phoneNumber("test")
                .role(Role.USER.getValue() + "," + Role.TEMP_JOB_SEEKER.getValue())
                .build();
        when(userRepository.save(any())).thenReturn(user);

        //when
        SignUpResponseDto response = userService.signUpCarOwner(carOwnerSignUpRequestDto, new ArrayList<>());

        //then
        assertThat(carOwnerSignUpRequestDto.getUserId()).isEqualTo(response.getUserId());
        assertThat(carOwnerSignUpRequestDto.getName()).isEqualTo(response.getName());
        assertThat(response.getRole()).isEqualTo(Role.USER.getValue() + "," + Role.TEMP_JOB_SEEKER.getValue());
    }
    @Test
    @DisplayName("회원가입 성공 - 차주 - 구인자")
    void signUpSuccessCarOwnerRecruiter() throws IOException {
        // given
        CarOwnerSignUpRequestDto carOwnerSignUpRequestDto = CarOwnerSignUpRequestDto.builder()
                .name("test")
                .carId(1L)
                .type(1)
                .userId("test")
                .fcmToken("test")
                .password("test")
                .phoneNumber("test")
                .build();
        User user = User.builder()
                .name("test")
                .userId("test")
                .password("test")
                .phoneNumber("test")
                .role(Role.USER.getValue() + "," + Role.TEMP_RECRUITER.getValue())
                .build();
        when(userRepository.save(any())).thenReturn(user);

        //when
        SignUpResponseDto response = userService.signUpCarOwner(carOwnerSignUpRequestDto, new ArrayList<>());

        //then
        assertThat(carOwnerSignUpRequestDto.getUserId()).isEqualTo(response.getUserId());
        assertThat(carOwnerSignUpRequestDto.getName()).isEqualTo(response.getName());
        assertThat(response.getRole()).isEqualTo(Role.USER.getValue() + "," + Role.TEMP_RECRUITER.getValue());
    }
    @Test
    @DisplayName("회원가입 성공 - 화주")
    void signUpSuccessOwner() throws IOException {
        // given
        OwnerSignUpRequestDto ownerSignUpRequestDto = OwnerSignUpRequestDto.builder()
                .userId("test")
                .companyLocation("test")
                .startedAt(new Date())
                .name("test")
                .phoneNumber("test")
                .fcmToken("test")
                .license("test")
                .fcmToken("test")
                .password("test")
                .build();
        User user = User.builder()
                .name("test")
                .userId("test")
                .password("test")
                .phoneNumber("test")
                .role(Role.USER.getValue() + "," + Role.TEMP_JOB_SEEKER.getValue())
                .build();
        when(userRepository.save(any())).thenReturn(user);

        //when
        SignUpResponseDto response = userService.signUpOwner(ownerSignUpRequestDto, new ArrayList<>());

        //then
        assertThat(ownerSignUpRequestDto.getUserId()).isEqualTo(response.getUserId());
        assertThat(ownerSignUpRequestDto.getName()).isEqualTo(response.getName());
        assertThat(response.getRole()).isEqualTo(Role.USER.getValue() + "," + Role.TEMP_JOB_SEEKER.getValue());
    }
    @Test
    @DisplayName("차주 회원가입 실패 - type overflow")
    void signUpCarOwnerFailWhenTypeOverflow(){
        // given
        CarOwnerSignUpRequestDto carOwnerSignUpRequestDto = CarOwnerSignUpRequestDto.builder()
                .name("test")
                .carId(1L)
                .type(2)
                .userId("test")
                .fcmToken("test")
                .password("test")
                .phoneNumber("test")
                .userId("test")
                .build();

        // when
        CustomException customException = assertThrows(CustomException.class, () -> userService.signUpCarOwner(carOwnerSignUpRequestDto, new ArrayList<>()));

        // then
        assertThat(customException.getMessage()).isEqualTo(ErrorCode.INVALID_PARAMETER.getMessage()+" 타입은 0 또는 1이어야 합니다.");
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INVALID_PARAMETER);
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
    @Test
    @DisplayName("차량 조회")
    void findAllCars(){
        // given
        List<Car> cars = setUpCars();
        when(carRepository.findAll()).thenReturn(cars);

        // when
        List<CarFindResponseDto> response = userService.findAllCars();

        // then
        assertThat(response).hasSize(10);
    }
    @Test
    @DisplayName("전화 번호로 유저 찾기")
    void findByPhoneNumber(){
        // given
        String correctPhoneNumber = "01012345678";
        UserIdFindRequestDto request = UserIdFindRequestDto.builder().phoneNumber(correctPhoneNumber).build();
        User user = setUpUser();
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(user));
        // when
        UserIdFindResponseDto response = userService.findUserIdByPhoneNumber(request);

        // then
        assertThat(response.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(response.getUserId()).isEqualTo("testU***");
    }
    private List<Car> setUpCars(){
        List<Car> cars = new ArrayList<>();
        for(int i =0; i<10; i++){
            cars.add(Car.builder()
                            .id(1L+i)
                            .category("test")
                    .build());
        }
        return cars;
    }


    private ChangePasswordRequestDto setUpChangePasswordRequestDto(){
        return ChangePasswordRequestDto.builder()
                .userId("testUser")
                .password("changedPassword")
                .build();
    }

    private UserSignUpRequestDto setUpSignUpRequestDto(){
        return UserSignUpRequestDto.builder()
                .name("test")
                .userId("test")
                .phoneNumber("test")
                .password("test")
                .build();
    }

    private User setUpUser(){
        return User.builder()
                .userId("testUser")
                .name("testName")
                .deleted(false)
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
                .password("testPassword")
                .role(Role.USER.getValue())
                .phoneNumber("01012345678")
                .refreshToken("testRefreshToken")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
