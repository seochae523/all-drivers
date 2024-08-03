package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.dto.request.BoardSaveRequestDto;
import com.alldriver.alldriver.board.dto.request.BoardUpdateRequestDto;
import com.alldriver.alldriver.board.dto.response.BoardDeleteResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardSaveResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardUpdateResponseDto;
import com.alldriver.alldriver.board.service.BoardCategoryRetrieveService;
import com.alldriver.alldriver.board.service.impl.BoardServiceImpl;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    @Test
    @DisplayName("저장 성공")
    void save() throws Exception {
        // given
        BoardSaveRequestDto boardSaveRequestDto = setUpBoardSaveRequest();
        BoardSaveResponseDto boardSaveResponseDto = setUpBoardSaveResponse();
        when(boardService.save(any(), any())).thenReturn(boardSaveResponseDto);

        // when, then
        mockMvc.perform(post("/user/board/save").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardSaveRequestDto)))
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

        // when, then
        mockMvc.perform(post("/user/board/save").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardSaveRequestDto)))
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

        // when, then
        mockMvc.perform(post("/user/board/save").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardSaveRequestDto)))
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
                .build();
        BoardUpdateResponseDto responseDto = BoardUpdateResponseDto.builder()
                .id(1L)
                .title("modified")
                .content("modified content")
                .build();
        when(boardService.update(any(), any())).thenReturn(responseDto);

        mockMvc.perform(put("/user/board/update").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modified)))
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
                .build();

        when(boardService.update(any(), any())).thenThrow(new CustomException(ErrorCode.BOARD_NOT_FOUND));

        mockMvc.perform(put("/user/board/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modified)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.BOARD_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.BOARD_NOT_FOUND.getCode()));
    }

    @Test
    @DisplayName("업데이트 실패 - 유저와 작성자 미일치")
    void updateFailWhenUserNotFound() throws Exception {
        BoardUpdateRequestDto modified = BoardUpdateRequestDto.builder()
                .id(1L)
                .title("modified")
                .content("modified content")
                .build();

        when(boardService.update(any(), any())).thenThrow(new CustomException(ErrorCode.INVALID_USER));

        mockMvc.perform(put("/user/board/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modified)))
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



    private BoardSaveRequestDto setUpBoardSaveRequest(){
        return BoardSaveRequestDto.builder()
                .title("test")
                .content("test")
                .build();
    }
    private BoardSaveResponseDto setUpBoardSaveResponse(){
        return BoardSaveResponseDto.builder()
                .title("test")
                .content("test")
                .build();
    }
}