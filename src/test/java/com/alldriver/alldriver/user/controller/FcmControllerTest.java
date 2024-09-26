package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.user.dto.request.FcmSendRequestDto;
import com.alldriver.alldriver.user.dto.request.RefreshRequestDto;
import com.alldriver.alldriver.user.service.impl.FcmServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FcmController.class)
@WithMockUser(username = "testUser", password = "1234")
class FcmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FcmServiceImpl fcmService;

    @Autowired
    private Validator validator;

    @Test
    void sendMessage() throws Exception {
        // given
        FcmSendRequestDto request = FcmSendRequestDto.builder()
                .title("test")
                .content("test")
                .build();
        String response = "알림 전송 성공";
        when(fcmService.sendMessage(any())).thenReturn(response);

        // when, then
        mockMvc.perform(post("/user/fcm/send").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    void sendMessageFailWhenTokenNotFound() throws Exception {
        FcmSendRequestDto request = FcmSendRequestDto.builder()
                .title("test")
                .content("test")
                .build();

        when(fcmService.sendMessage(any())).thenThrow(new CustomException(ErrorCode.FCM_TOKEN_NOT_FOUND));

        mockMvc.perform(post("/user/fcm/send").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.FCM_TOKEN_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.FCM_TOKEN_NOT_FOUND.getMessage()));
    }
    @Test
    void sendMessageFailWhenSendFail() throws Exception {
        FcmSendRequestDto request = FcmSendRequestDto.builder()
                .title("test")
                .content("test")
                .build();

        when(fcmService.sendMessage(any())).thenThrow(new CustomException(ErrorCode.FCM_SEND_FAIL));

        mockMvc.perform(post("/user/fcm/send").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.FCM_SEND_FAIL.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.FCM_SEND_FAIL.getMessage()));
    }

    @Test
    void validateRequestDto(){
        FcmSendRequestDto request = FcmSendRequestDto.builder().build();

        // when
        Set<ConstraintViolation<FcmSendRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<FcmSendRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<FcmSendRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.TITLE_NOT_FOUND,
                ValidationError.Message.CONTENT_NOT_FOUND);
    }
}