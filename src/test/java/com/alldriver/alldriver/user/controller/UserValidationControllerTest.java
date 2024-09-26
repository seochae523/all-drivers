package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.user.dto.request.PhoneNumberCheckRequestDto;
import com.alldriver.alldriver.user.service.impl.UserValidationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
@WebMvcTest(UserValidationController.class)
@WithMockUser(username = "testUser", password = "1234")
class UserValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserValidationServiceImpl userValidationService;

    @Test
    @DisplayName("중복 회원 id 탐지 - 중복 있을 때")
    void checkDuplicatedUserId() throws Exception {
        //given
        String userId="testUser";
        when(userValidationService.checkDuplicatedAccount(userId)).thenThrow(new CustomException(ErrorCode.DUPLICATED_ACCOUNT));

        //when, then
        mockMvc.perform(get("/check/user-id").with(csrf())
                        .param("userId", userId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATED_ACCOUNT.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATED_ACCOUNT.getMessage()));
    }
    @Test
    @DisplayName("중복 회원 id 탐지 - 중복 없을 때")
    void checkUnDuplicatedUserId() throws Exception {
        //given
        String userId="testUser";
        when(userValidationService.checkDuplicatedAccount(userId)).thenReturn(true);

        //when, then
        mockMvc.perform(get("/check/user-id").with(csrf())
                        .param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.valueOf(true).toString()));
    }

    @Test
    @DisplayName("중복 사업자 번호 탐지 - 중복 있을 때")
    void checkDuplicatedLicense() throws Exception {
        //given
        String license = "testLicense";
        when(userValidationService.checkLicense(license)).thenThrow(new CustomException(ErrorCode.DUPLICATED_LICENSE_NUMBER));

        //when, then
        mockMvc.perform(get("/check/license")
                        .param("license", license))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATED_LICENSE_NUMBER.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATED_LICENSE_NUMBER.getMessage()));
    }
    @Test
    @DisplayName("중복 사업자 번호 탐지 - 중복 없을 때")
    void checkUnDuplicatedLicense() throws Exception {
        //given
        String license = "testLicense";
        when(userValidationService.checkLicense(license)).thenReturn(true);


        //when, then
        mockMvc.perform(get("/check/license").with(csrf())
                        .param("license", license))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.valueOf(true).toString()));
    }


    @Test
    @DisplayName("회원 가입 전화 번호 검증 - 중복 없을 때")
    void checkUnDuplicatedPhoneNumberWhenSignUp() throws Exception{
        // given
        String phoneNumber = "01012345678";
        Integer type = 0;
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = PhoneNumberCheckRequestDto.builder().phoneNumber(phoneNumber).type(type).build();
        when(userValidationService.checkPhoneNumber(any())).thenReturn(true);

        //when, then
        mockMvc.perform(post("/check/phoneNumber").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(phoneNumberCheckRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.valueOf(true).toString()));

    }
    @Test
    @DisplayName("회원 가입 전화 번호 검증 - 중복 있을 때")
    void checkDuplicatedPhoneNumberWhenSignUp() throws Exception {
        // given
        String phoneNumber = "01012345678";
        Integer type = 0;
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = PhoneNumberCheckRequestDto.builder().phoneNumber(phoneNumber).type(type).build();
        when(userValidationService.checkPhoneNumber(any())).thenThrow(new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER));

        // when, then
        mockMvc.perform(post("/check/phoneNumber").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneNumberCheckRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATED_PHONE_NUMBER.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATED_PHONE_NUMBER.getMessage()));
    }

    @Test
    @DisplayName("비밀번호 변경 전화 번호 검증 - 사용자 있을 때")
    void checkUnDuplicatedPhoneNumberWhenChangePassword() throws Exception{
        // given
        String phoneNumber = "01012345678";
        Integer type = 1;
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = PhoneNumberCheckRequestDto.builder().phoneNumber(phoneNumber).type(type).build();
        when(userValidationService.checkPhoneNumber(any())).thenReturn(true);

        //when, then
        mockMvc.perform(post("/check/phoneNumber").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneNumberCheckRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.valueOf(true).toString()));

    }
    @Test
    @DisplayName("비밀번호 변경 전화 번호 검증 - 사용자 없을 때")
    void checkDuplicatedPhoneNumberWhenChangePassword() throws Exception {
        // given
        String phoneNumber = "01012345678";
        Integer type = 1;
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = PhoneNumberCheckRequestDto.builder().phoneNumber(phoneNumber).type(type).build();
        when(userValidationService.checkPhoneNumber(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // when, then
        mockMvc.perform(post("/check/phoneNumber").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneNumberCheckRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));
    }
    @Test
    @DisplayName("잊어 버린 비밀번호 변경 전화 번호 검증 - 사용자 있을 때")
    void checkDuplicatedPhoneNumberWhenChangeForgetPassword() throws Exception {
        // given
        String phoneNumber = "01012345678";
        String userId = "test";
        Integer type = 2;
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = PhoneNumberCheckRequestDto.builder().phoneNumber(phoneNumber).userId(userId).type(type).build();
        when(userValidationService.checkPhoneNumber(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // when, then
        mockMvc.perform(post("/check/phoneNumber").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneNumberCheckRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));
    }
    @Test
    @DisplayName("잊어 버린 비밀번호 변경 전화 번호 검증 - 사용자 없을 때")
    void checkUnDuplicatedPhoneNumberWhenChangeForgetPassword() throws Exception {
        // given
        String phoneNumber = "01012345678";
        String userId = "test";
        Integer type = 2;
        PhoneNumberCheckRequestDto phoneNumberCheckRequestDto = PhoneNumberCheckRequestDto.builder().phoneNumber(phoneNumber).userId(userId).type(type).build();
        when(userValidationService.checkPhoneNumber(any())).thenReturn(true);

        // when, then
        mockMvc.perform(post("/check/phoneNumber").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneNumberCheckRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.valueOf(true).toString()));

    }
}