package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.common.exception.JwtException;
import com.alldriver.alldriver.user.dto.request.RefreshRequestDto;
import com.alldriver.alldriver.user.dto.request.SmsVerifyRequestDto;
import com.alldriver.alldriver.user.dto.response.AuthToken;
import com.alldriver.alldriver.user.service.impl.RefreshServiceImpl;
import com.alldriver.alldriver.user.service.impl.SmsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RefreshController.class)
@WithMockUser(username = "testUser", password = "1234")
class RefreshControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RefreshServiceImpl refreshService;

    @Autowired
    private Validator validator;
    @Test
    @DisplayName("리프래쉬 성공")
    void refresh() throws Exception {
        //given
        RefreshRequestDto request = RefreshRequestDto.builder().refreshToken("test").build();
        AuthToken response = AuthToken.builder().accessToken("test").refreshToken("test").grantType("test").build();
        when(refreshService.refresh(any())).thenReturn(response);

        //when, then
        mockMvc.perform(post("/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(print());
    }

    @Test
    @DisplayName("리프래쉬 실패")
    void refreshFail() throws Exception {
        //given
        RefreshRequestDto request = RefreshRequestDto.builder().refreshToken("test").build();
        when(refreshService.refresh(any())).thenThrow(new JwtException(ErrorCode.INVALID_AUTH_TOKEN));

        //when, then
        mockMvc.perform(post("/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_AUTH_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_AUTH_TOKEN.getMessage()))
                .andDo(print());

    }

    @Test
    @DisplayName("리프래쉬 파라미터 검증")
    void validateRefreshDto(){
        //given
        RefreshRequestDto request = new RefreshRequestDto();

        // when
        Set<ConstraintViolation<RefreshRequestDto>> validate = validator.validate(request);
        Iterator<ConstraintViolation<RefreshRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<RefreshRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.REFRESH_TOKEN_NOT_FOUND);
    }
}