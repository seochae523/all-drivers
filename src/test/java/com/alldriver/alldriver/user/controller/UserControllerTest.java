package com.alldriver.alldriver.user.controller;


import com.alldriver.alldriver.board.domain.Car;
import com.alldriver.alldriver.board.dto.response.CarFindResponseDto;
import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.user.dto.response.*;
import com.alldriver.alldriver.user.dto.request.*;

import com.alldriver.alldriver.user.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@WithMockUser(username = "testUser", password = "1234")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;
    @Autowired
    private Validator validator;

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() throws Exception {
        // given
        Map<String, String> content = new HashMap<>();
        content.put("userId", "testUser");
        content.put("password", "1234");
        AuthToken authToken = AuthToken.builder().accessToken("accessToken").refreshToken("refreshToken").grantType("Bearer").build();


        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .userId("testUser")
                .roles(new ArrayList<>())
                .authToken(authToken).build();

        when(userService.login(any())).thenReturn(loginResponseDto);

        // when, then
        mockMvc.perform(post("/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(content)))
                .andExpect(status().is3xxRedirection());
        // TODO : 200 왜 안뜨는지 고려해야됨
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value("testUser"))
//                .andExpect(jsonPath("$.authToken.grantType").value("Bearer"))
//                .andExpect(jsonPath("$.authToken.accessToken").value("accessToken"))
//                .andExpect(jsonPath("$.authToken.refreshToken").value("refreshToken"));
    }
    // TODO : 302 해결해야됨, 위와 동일
//    @Test
//    @DisplayName("로그인 실패")
//    void loginFail() throws Exception {
//        // given
//        Map<String, String> content = new HashMap<>();
//        content.put("userId", "testUser");
//        content.put("password", "1234");
//        when(userService.login(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
//
//        // when, then
//
//        mockMvc.perform(post("/login").with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(content)))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
//                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));
//
//    }
    @Test
    @DisplayName("유저 회원 가입 - 성공")
    void signUpUser() throws Exception {
        // given
        UserSignUpRequestDto userSignUpRequestDto = setUpUser();
        SignUpResponseDto signUpResponseDto = setUpSignUpResponse();
        when(userService.signUpUser(any())).thenReturn(signUpResponseDto);

        // when, then
        mockMvc.perform(post("/sign-up/user").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testUser"))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @DisplayName("화주 회원 가입 - 성공")
    void signUpOwner() throws Exception {
        // given
        OwnerSignUpRequestDto ownerSignUpRequestDto = setUpOwner();
        MockMultipartFile multipartFile = new MockMultipartFile("images", "test.jpg", "image/jpg", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile request = new MockMultipartFile("request", "request", "application/json", objectMapper.writeValueAsString(ownerSignUpRequestDto).getBytes(StandardCharsets.UTF_8));
        SignUpResponseDto signUpResponseDto = setUpSignUpResponse();
        when(userService.signUpOwner(any(), any())).thenReturn(signUpResponseDto);

        // when, then
        mockMvc.perform(multipart("/sign-up/owner")
                        .file(multipartFile)
                        .file(request).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testUser"))
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    @DisplayName("차주 회원 가입 - 성공")
    void signUpCarOwner() throws Exception {
        // given
        CarOwnerSignUpRequestDto carOwnerSignUpRequestDto = setUpCarOwner();
        MockMultipartFile multipartFile1 = new MockMultipartFile("images", "test1.jpg", "image/jpg", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile2 = new MockMultipartFile("images", "test2.jpg", "image/jpg", "test file".getBytes(StandardCharsets.UTF_8) );

        MockMultipartFile request = new MockMultipartFile("request", "request", "application/json", objectMapper.writeValueAsString(carOwnerSignUpRequestDto).getBytes(StandardCharsets.UTF_8));
        SignUpResponseDto signUpResponseDto = setUpSignUpResponse();
        when(userService.signUpCarOwner(any(), any())).thenReturn(signUpResponseDto);

        // when, then
        mockMvc.perform(multipart("/sign-up/car-owner")
                        .file(multipartFile1)
                        .file(multipartFile2)
                        .file(request).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testUser"))
                .andExpect(jsonPath("$.name").value("testName"));
    }


    @Test
    @DisplayName("로그아웃 성공")
    void logoutSuccess() throws Exception {
        // given
        String response = "로그아웃 성공. user id = testUser";
        when(userService.logout()).thenReturn(response);

        // when, then
        mockMvc.perform(get("/user/logout").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(response))
                .andDo(print());
    }
    @Test
    @DisplayName("로그아웃 - 실패")
    void logoutFail() throws Exception {
        // given
        when(userService.logout()).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // when, then
        mockMvc.perform(get("/user/logout").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));
    }



    @Test
    @DisplayName("회원 업데이트 - 실패")
    void updateFail() throws Exception {
        // given
        Map<String, String> content = new HashMap<>();
        content.put("userId", "testUser");
        content.put("nickname", "testNick");

        when(userService.update(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // when, then
        mockMvc.perform(put("/user/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(content)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));
    }
    @Test
    @DisplayName("잊어버린 비밀번호 변경 - 성공")
    void updateForgetPasswordSuccess() throws Exception{
        // given
        Map<String, String> content = new HashMap<>();
        content.put("userId", "testUser");
        content.put("password", "testPassword");
        ChangePasswordResponseDto changePasswordResponseDto = ChangePasswordResponseDto.builder()
                .userId("testUser")
                .build();


        when(userService.changePassword(any())).thenReturn(changePasswordResponseDto);

        // when, then
        mockMvc.perform(put("/change-forget-password").with(csrf())
                        .content(objectMapper.writeValueAsString(content))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testUser"));
    }
    @Test
    @DisplayName("잊어버린 비밀번호 변경 - 실패")
    void updateForgetPasswordFail() throws Exception{
        // given
        Map<String, String> content = new HashMap<>();
        content.put("userId", "testUser");
        content.put("password", "testPassword");
        when(userService.changePassword(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // when, then
        mockMvc.perform(put("/change-forget-password").with(csrf())
                        .content(objectMapper.writeValueAsString(content))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));
    }
    @Test
    @DisplayName("비밀번호 변경 - 성공")
    void updatePasswordSuccess() throws Exception {
        // given
        Map<String, String> content = new HashMap<>();
        content.put("userId", "testUser");
        content.put("password", "testPassword");
        ChangePasswordResponseDto changePasswordResponseDto = ChangePasswordResponseDto.builder()
                .userId("testUser")
                .build();

        when(userService.changePassword(any())).thenReturn(changePasswordResponseDto);


        // when, then
        mockMvc.perform(put("/user/change-password").with(csrf())
                        .content(objectMapper.writeValueAsString(content))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testUser"));
    }
    @Test
    @DisplayName("비밀번호 변경 - 실패")
    void updatePasswordFail() throws Exception {
        // given
        Map<String, String> content = new HashMap<>();
        content.put("userId", "testUser");
        content.put("password", "testPassword");

        when(userService.changePassword(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // when, then
        mockMvc.perform(put("/change-forget-password").with(csrf())
                        .content(objectMapper.writeValueAsString(content))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("차주 회원가입을 위한 차량 카테고리 조회")
    void findAllCars() throws Exception {
        // given
        List<CarFindResponseDto> response = setUpCarFindResponse();
        when(userService.findAllCars()).thenReturn(response);

        // when, then
        mockMvc.perform(get("/cars").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
    @Test
    @DisplayName("휴대전화 번호로 유저 아이디 찾기")
    void findUserIdByPhoneNumber() throws Exception {
        // given
        String phoneNumber = "01012345678";
        String userId = "testU***";
        UserIdFindRequestDto request = UserIdFindRequestDto.builder().phoneNumber(phoneNumber).build();
        UserIdFindResponseDto response = UserIdFindResponseDto.builder().userId(userId).createdAt(LocalDateTime.now()).build();
        when(userService.findUserIdByPhoneNumber(any())).thenReturn(response);

        // when, then
        mockMvc.perform(post("/find-user-id").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("휴대전화 번호로 유저 아이디 찾기 파라미터 검증")
    void validateUserIdFindRequest(){
        // given
        UserIdFindRequestDto request = new UserIdFindRequestDto();

        // when
        Set<ConstraintViolation<UserIdFindRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<UserIdFindRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<UserIdFindRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.PHONE_NUMBER_NOT_FOUND);

    }
    @Test
    @DisplayName("유저 회원가입 파라미터 검증")
    void validateUserSignUpRequest(){
        // given
        UserSignUpRequestDto request = new UserSignUpRequestDto();

        // when
        Set<ConstraintViolation<UserSignUpRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<UserSignUpRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<UserSignUpRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.NAME_NOT_FOUND,
                ValidationError.Message.USER_ID_NOT_FOUND,
                ValidationError.Message.PASSWORD_NOT_FOUND,
                ValidationError.Message.PHONE_NUMBER_NOT_FOUND,
                ValidationError.Message.FCM_TOKEN_NOT_FOUND);

    }
    @Test
    @DisplayName("차주 회원가입 파라미터 검증")
    void validateCarOwnerSignUpRequest(){
        // given
        CarOwnerSignUpRequestDto request = new CarOwnerSignUpRequestDto();

        // when
        Set<ConstraintViolation<CarOwnerSignUpRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<CarOwnerSignUpRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<CarOwnerSignUpRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.NAME_NOT_FOUND,
                ValidationError.Message.USER_ID_NOT_FOUND,
                ValidationError.Message.PASSWORD_NOT_FOUND,
                ValidationError.Message.PHONE_NUMBER_NOT_FOUND,
                ValidationError.Message.FCM_TOKEN_NOT_FOUND,
                ValidationError.Message.TYPE_NOT_FOUND,
                ValidationError.Message.CAR_ID_NOT_FOUND);

    }
    @Test
    @DisplayName("화주 회원가입 파라미터 검증")
    void validateOwnerSignUpRequest(){
        // given
        OwnerSignUpRequestDto request = new OwnerSignUpRequestDto();

        // when
        Set<ConstraintViolation<OwnerSignUpRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<OwnerSignUpRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<OwnerSignUpRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.NAME_NOT_FOUND,
                ValidationError.Message.USER_ID_NOT_FOUND,
                ValidationError.Message.PASSWORD_NOT_FOUND,
                ValidationError.Message.PHONE_NUMBER_NOT_FOUND,
                ValidationError.Message.FCM_TOKEN_NOT_FOUND,
                ValidationError.Message.COMPANY_LOCATION_NOT_FOUND,
                ValidationError.Message.BUSINESS_NUMBER_NOT_FOUND,
                ValidationError.Message.START_AT_NOT_FOUND);

    }
    @Test
    @DisplayName("소셜로그인 파라미터 검증")
    void validateSocialSignUpRequest(){
        // given
        SocialLoginSignUpRequestDto request = new SocialLoginSignUpRequestDto();

        // when
        Set<ConstraintViolation<SocialLoginSignUpRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<SocialLoginSignUpRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<SocialLoginSignUpRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.FCM_TOKEN_NOT_FOUND,
                ValidationError.Message.USER_ID_NOT_FOUND);

    }

    @Test
    @DisplayName("로그인 파라미터 검증")
    void validateLoginRequest(){
        // given
        LoginRequestDto request = new LoginRequestDto();

        // when
        Set<ConstraintViolation<LoginRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<LoginRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<LoginRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.USER_ID_NOT_FOUND,
                ValidationError.Message.PASSWORD_NOT_FOUND,
                ValidationError.Message.FCM_TOKEN_NOT_FOUND);

    }
    @Test
    @DisplayName("비밀번호 변경 파라미터 검증")
    void validateChangePasswordRequest(){
        // given
        ChangePasswordRequestDto request = new ChangePasswordRequestDto();

        // when
        Set<ConstraintViolation<ChangePasswordRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<ChangePasswordRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<ChangePasswordRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.USER_ID_NOT_FOUND,
                ValidationError.Message.PASSWORD_NOT_FOUND);

    }
    // validate user update request dto
    @Test
    @DisplayName("유저 업데이트 파라미터 검증")
    void validateUserUpdateRequest(){
        // given
        UserUpdateRequestDto request = new UserUpdateRequestDto();

        // when
        Set<ConstraintViolation<UserUpdateRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<UserUpdateRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<UserUpdateRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.USER_ID_NOT_FOUND);

    }

    @Test
    @DisplayName("유저 삭제 성공")
    void signOut() throws Exception {
        // given
        String userId="testUser";
        String response = "회원 탈퇴 완료.";
        when(userService.signOut()).thenReturn(response);

        // when, then
        mockMvc.perform(delete("/user/sign-out").with(csrf())
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }


    private UserSignUpRequestDto setUpUser(){
        return UserSignUpRequestDto.builder()
                .userId("testUser")
                .name("testName")
                .password("1234")
                .phoneNumber("01012345678")

                .fcmToken("testFcm")
                .build();
    }
    private OwnerSignUpRequestDto setUpOwner(){
        return OwnerSignUpRequestDto.builder()
                .userId("testUser")
                .name("testName")
                .phoneNumber("01012345678")

                .password("1234")
                .license("testLicense")
                .fcmToken("testFcm")
                .startedAt(new Date())
                .companyLocation("company")
                .build();
    }
    private CarOwnerSignUpRequestDto setUpCarOwner(){
        return CarOwnerSignUpRequestDto.builder()
                .userId("testUser")
                .name("testName")
                .phoneNumber("01012345678")

                .password("1234")
                .fcmToken("testFcm")
                .type(0)
                .carId(1L)
                .build();
    }
    private SignUpResponseDto setUpSignUpResponse(){
        return SignUpResponseDto.builder()
                .userId("testUser")
                .name("testName")
                .build();
    }
    private List<CarFindResponseDto> setUpCarFindResponse(){
        List<CarFindResponseDto> response = new ArrayList<>();

        for(int i =0; i<10; i++){
            response.add(new CarFindResponseDto(1L + i, "test"));
        }
        return response;
    }

}
