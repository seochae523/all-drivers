package com.alldriver.alldriver.board.controller;


import com.alldriver.alldriver.board.dto.request.*;
import com.alldriver.alldriver.board.dto.response.BoardDeleteResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardSaveResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardUpdateResponseDto;

import com.alldriver.alldriver.board.service.impl.BoardServiceImpl;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.common.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.Errors;



import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardController.class)
@WithMockUser("test")
class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    BoardServiceImpl boardService;

    @Autowired
    private Validator validator;
    @Test
    @DisplayName("저장 성공")
    void save() throws Exception {
        // given
        BoardSaveRequestDto boardSaveRequestDto = setUpBoardSaveRequest();
        BoardSaveResponseDto boardSaveResponseDto = setUpBoardSaveResponse();
        when(boardService.save(any(), any())).thenReturn(boardSaveResponseDto);
        MockMultipartFile request = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(boardSaveRequestDto).getBytes(StandardCharsets.UTF_8));
        // when, then
        mockMvc.perform(multipart("/user/board/save")
                .file(request).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.content").value("test"));
    }

    @Test
    @DisplayName("저장 실패 - 유저 미존재")
    void saveFailWhenUserNotFound() throws Exception {
        // given
        BoardSaveRequestDto boardSaveRequestDto = setUpBoardSaveRequest();

        when(boardService.save(any(), any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        MockMultipartFile request = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(boardSaveRequestDto).getBytes(StandardCharsets.UTF_8));

        // when, then
        mockMvc.perform(multipart("/user/board/save")
                        .file(request).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()));
    }
    @Test
    @DisplayName("저장 실패 - main location 미존재")
    void saveFailWhenMainLocationNotFound() throws Exception {
        // given
        BoardSaveRequestDto boardSaveRequestDto = setUpBoardSaveRequest();

        when(boardService.save(any(), any())).thenThrow(new CustomException(ErrorCode.MAIN_LOCATION_NOT_FOUND));
        MockMultipartFile request = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(boardSaveRequestDto).getBytes(StandardCharsets.UTF_8));

        // when, then
        mockMvc.perform(multipart("/user/board/save")
                        .file(request).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.MAIN_LOCATION_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.MAIN_LOCATION_NOT_FOUND.getCode()));
    }

    @Test
    @DisplayName("업데이트 성공")
    void update() throws Exception {
        BoardUpdateRequestDto modified = BoardUpdateRequestDto.builder()
                .id(1L)
                .title("modified")
                .content("modified content")
                .endAt(new Date())
                .startAt(new Date())
                .category("cat")
                .carInfos(List.of(new CarUpdateRequestDto(0, 1L)))
                .jobInfos(List.of(new JobUpdateRequestDto(0, 1L)))
                .locationInfos(List.of(new LocationUpdateRequestDto(0, 1L)))
                .payType("type")
                .mainLocationId(1L)
                .recruitType("req")
                .companyLocation("comp")
                .payment(123)
                .build();

        BoardUpdateResponseDto responseDto = BoardUpdateResponseDto.builder()
                .id(1L)
                .title("modified")
                .content("modified content")
                .build();


        when(boardService.update(any(), any())).thenReturn(responseDto);
        MockMultipartFile request = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(modified).getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(HttpMethod.PUT, "/user/board/update")
                        .file(request).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("modified"))
                .andExpect(jsonPath("$.content").value("modified content"));
    }

    @Test
    @DisplayName("업데이트 실패 - 게시판 미존재")
    void updateFailWhenBoardNotFound() throws Exception {
        BoardUpdateRequestDto modified = BoardUpdateRequestDto.builder()
                .id(1L)
                .title("modified")
                .content("modified content")
                .endAt(new Date())
                .startAt(new Date())
                .category("cat")
                .carInfos(List.of(new CarUpdateRequestDto(0, 1L)))
                .jobInfos(List.of(new JobUpdateRequestDto(0, 1L)))
                .locationInfos(List.of(new LocationUpdateRequestDto(0, 1L)))
                .payType("type")
                .recruitType("rec")
                .companyLocation("comp")
                .mainLocationId(1L)
                .payment(123)
                .build();

        when(boardService.update(any(), any())).thenThrow(new CustomException(ErrorCode.BOARD_NOT_FOUND));
        MockMultipartFile request = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(modified).getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(HttpMethod.PUT, "/user/board/update")
                        .file(request).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.BOARD_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.BOARD_NOT_FOUND.getCode()));
    }

    @Test
    @DisplayName("업데이트 실패 - 유저와 작성자 미일치")
    void updateFailWhenUserNotFound() throws Exception {
        // given
        BoardUpdateRequestDto modified = BoardUpdateRequestDto.builder()
                .id(1L)
                .title("modified")
                .content("modified content")
                .companyLocation("comp")
                .endAt(new Date())
                .startAt(new Date())
                .category("cat")
                .recruitType("rec")
                .carInfos(List.of(new CarUpdateRequestDto(0, 1L)))
                .jobInfos(List.of(new JobUpdateRequestDto(0, 1L)))
                .locationInfos(List.of(new LocationUpdateRequestDto(0, 1L)))
                .payType("type")
                .mainLocationId(1L)
                .payment(123)
                .build();

        when(boardService.update(any(), any())).thenThrow(new CustomException(ErrorCode.INVALID_USER));
        MockMultipartFile request = new MockMultipartFile("request", null, "application/json", objectMapper.writeValueAsString(modified).getBytes(StandardCharsets.UTF_8));

        // when, then
        mockMvc.perform(multipart(HttpMethod.PUT, "/user/board/update")
                        .file(request).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_USER.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_USER.getCode()));
    }

    @Test
    @DisplayName("삭제 성공")
    void delete() throws Exception {
        Long id = 1L;
        BoardDeleteResponseDto responseDto = BoardDeleteResponseDto.builder()
                .id(1L)
                .build();
        when(boardService.delete(any())).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/board/delete/" + id).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("게시판 저장 파라미터 검증")
    void validateBoardSaveRequest(){
        // given
        BoardSaveRequestDto boardSaveRequestDto = BoardSaveRequestDto.builder().build();

        // when
        Set<ConstraintViolation<BoardSaveRequestDto>> validate = validator.validate(boardSaveRequestDto);
        Iterator<ConstraintViolation<BoardSaveRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<BoardSaveRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        // then
        Assertions.assertThat(messages).contains(ValidationError.Message.CONTENT_NOT_FOUND,
                ValidationError.Message.CATEGORY_NOT_FOUND,
                ValidationError.Message.TITLE_NOT_FOUND,
                ValidationError.Message.PAY_TYPE_NOT_FOUND,
                ValidationError.Message.PAYMENT_NOT_FOUND,
                ValidationError.Message.START_AT_NOT_FOUND,
                ValidationError.Message.END_AT_NOT_FOUND,
                ValidationError.Message.RECRUIT_TYPE_NOT_FOUND,
                ValidationError.Message.COMPANY_LOCATION_NOT_FOUND,
                ValidationError.Message.MAIN_LOCATION_ID_NOT_FOUND,
                ValidationError.Message.SUB_LOCATION_ID_NOT_FOUND,
                ValidationError.Message.CAR_ID_NOT_FOUND,
                ValidationError.Message.JOB_ID_NOT_FOUND);

    }

    @Test
    @DisplayName("게시판 업데이트 파라미터 검증")
    void validateBoardUpdateRequestDto(){
        BoardUpdateRequestDto boardUpdateRequestDto = BoardUpdateRequestDto.builder().build();

        Set<ConstraintViolation<BoardUpdateRequestDto>> validate = validator.validate(boardUpdateRequestDto);
        Iterator<ConstraintViolation<BoardUpdateRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<BoardUpdateRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }

        Assertions.assertThat(messages).contains(
                ValidationError.Message.BOARD_ID_NOT_FOUND,
                ValidationError.Message.CONTENT_NOT_FOUND,
                ValidationError.Message.CATEGORY_NOT_FOUND,
                ValidationError.Message.TITLE_NOT_FOUND,
                ValidationError.Message.PAY_TYPE_NOT_FOUND,
                ValidationError.Message.PAYMENT_NOT_FOUND,
                ValidationError.Message.RECRUIT_TYPE_NOT_FOUND,
                ValidationError.Message.COMPANY_LOCATION_NOT_FOUND,
                ValidationError.Message.START_AT_NOT_FOUND,
                ValidationError.Message.END_AT_NOT_FOUND,
                ValidationError.Message.MAIN_LOCATION_ID_NOT_FOUND
        );
    }

    private BoardSaveRequestDto setUpBoardSaveRequest(){
        return BoardSaveRequestDto.builder()
                .title("test")
                .content("test")
                .car(List.of(1L))
                .job(List.of(1L))
                .subLocations(List.of(1L))
                .category("cate")
                .companyLocation("compLoc")
                .endAt(new Date())
                .startAt(new Date())
                .mainLocationId(1L)
                .recruitType("req")
                .payment(123)
                .payType("type")
                .build();
    }
    private BoardSaveResponseDto setUpBoardSaveResponse(){
        return BoardSaveResponseDto.builder()
                .title("test")
                .content("test")
                .build();
    }
}