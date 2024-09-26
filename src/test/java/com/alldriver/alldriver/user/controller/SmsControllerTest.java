package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.user.dto.request.CarOwnerSignUpRequestDto;
import com.alldriver.alldriver.user.dto.request.SmsSendRequestDto;
import com.alldriver.alldriver.user.dto.request.SmsVerifyRequestDto;
import com.alldriver.alldriver.user.dto.response.SmsVerifyResponseDto;
import com.alldriver.alldriver.user.service.impl.SmsServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jdk.jfr.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(SmsController.class)
@WithMockUser(username = "testUser", password = "1234")
class SmsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SmsServiceImpl smsService;

    @Autowired
    private Validator validator;


    @Test
    @DisplayName("문자 발송 성공")
    void sendSmsAuthCode() throws Exception {
        //given
        String response = "문자 발송 완료";
        SmsSendRequestDto request = SmsSendRequestDto.builder().phoneNumber("01012345678").build();

        when(smsService.sendAuthCode(any())).thenReturn(response);

        //when, then
        mockMvc.perform(post("/sms/send").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(response))
                .andDo(print());
    }

    @Test
    @DisplayName("문자 인증 성공")
    void verifyAuthCode() throws Exception {
        //given
        SmsVerifyRequestDto request = SmsVerifyRequestDto.builder().phoneNumber("01012345678").authCode("123456").build();
        SmsVerifyResponseDto response = SmsVerifyResponseDto.builder().phoneNumber("01012345678").authResult(true).authCode("123456").build();
        when(smsService.verifiedCode(any())).thenReturn(response);

        //when, then
        mockMvc.perform(post("/sms/verify").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("01012345678"))
                .andExpect(jsonPath("$.authResult").value(true))
                .andExpect(jsonPath("$.authCode").value("123456"));
    }

    @Test
    @DisplayName("문자 발송 실패 - 핸드폰 번호 크기 미일치")
    void sendAuthCodeFailed() throws Exception {
        //given
        SmsSendRequestDto request = SmsSendRequestDto.builder().phoneNumber("0101234567").build();
        when(smsService.sendAuthCode(any())).thenThrow(new CustomException(ErrorCode.INVALID_PARAMETER));

        //when, then
        mockMvc.perform(post("/sms/send").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PARAMETER.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_PARAMETER.getMessage()));
    }

    @Test
    @DisplayName("문자 인증 실패 - 인증번호 불일치")
    void verifyAuthCodeFailed() throws Exception {
        //given
        SmsVerifyRequestDto request = SmsVerifyRequestDto.builder().phoneNumber("01012345678").authCode("123456").build();
        SmsVerifyResponseDto response = SmsVerifyResponseDto.builder().phoneNumber("01012345678").authResult(false).authCode("123456").build();
        when(smsService.verifiedCode(any())).thenReturn(response);

        //when, then
        mockMvc.perform(post("/sms/verify").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("01012345678"))
                .andExpect(jsonPath("$.authResult").value(false))
                .andExpect(jsonPath("$.authCode").value("123456"));
    }
    @Test
    @DisplayName("문자 발송 파라미터 검증")
    void validateSmsSendRequestDto(){
        //given

        SmsSendRequestDto request = new SmsSendRequestDto();

        // when
        Set<ConstraintViolation<SmsSendRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<SmsSendRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<SmsSendRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.PHONE_NUMBER_NOT_FOUND);
    }
    @Test
    @DisplayName("문자 인증 파라미터 검증")
    void validateAuthRequestDto(){
        //given
        SmsVerifyRequestDto request = new SmsVerifyRequestDto();

        // when
        Set<ConstraintViolation<SmsVerifyRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<SmsVerifyRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<SmsVerifyRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.PHONE_NUMBER_NOT_FOUND,
                ValidationError.Message.AUTH_CODE_NOT_FOUND);
    }
}
