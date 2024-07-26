package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.dto.request.BoardSaveRequestDto;
import com.alldriver.alldriver.board.dto.request.BoardUpdateRequestDto;
import com.alldriver.alldriver.board.dto.response.BoardSaveResponseDto;
import com.alldriver.alldriver.board.service.BoardCategoryRetrieveService;
import com.alldriver.alldriver.board.service.impl.BoardServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    void update() throws Exception {
        BoardUpdateRequestDto modified = BoardUpdateRequestDto.builder()
                .id(1L)
                .title("modified")
                .build();

        when(boardService.update(any(), any())).thenReturn("게시글 업데이트 완료.");

        mockMvc.perform(put("/user/board/update").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modified)))
                .andExpect(status().isOk())
                .andExpect(content().string("게시글 업데이트 완료."));
    }

    @Test
    void delete() throws Exception {
        Long id = 1L;

        when(boardService.delete(any())).thenReturn("게시글 삭제 완료.");

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/board/delete").with(csrf())
                        .param("boardId", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("게시글 삭제 완료."));
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