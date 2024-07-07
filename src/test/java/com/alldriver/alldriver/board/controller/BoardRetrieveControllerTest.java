package com.alldriver.alldriver.board.controller;


import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;

import com.alldriver.alldriver.board.service.BoardRetrieveService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardRetrieveController.class)
@WithMockUser("test")
class BoardRetrieveControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BoardRetrieveService boardRetrieveService;
    private final Integer page = 0;
    private final String requestPrefix ="/owner/board";
    @Test
    @DisplayName("게시판 전체 조회")
    void findAllBoards() throws Exception {
        // given
        List<BoardFindResponseDto> boardFindResponseDtos = setUpBoardFindResponseDto();
        when(boardRetrieveService.findAll(page)).thenReturn(boardFindResponseDtos);

        // when, then
        mockMvc.perform(get(requestPrefix+ "/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(boardFindResponseDtos)));
    }

    @Test
    @DisplayName("게시판 차량 정보로 조회")
    void findByCars() throws Exception {
        // given
        List<Long> carIds = setUpIds();

        List<BoardFindResponseDto> boardFindResponseDtos = setUpBoardFindResponseDto();
        when(boardRetrieveService.findByCars(page, carIds)).thenReturn(boardFindResponseDtos);

        // when, then
        mockMvc.perform(get(requestPrefix+"/cars")
                        .param("carIds", carIds.stream().map(String::valueOf).toArray(String[]::new)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(boardFindResponseDtos)));

    }

    @Test
    @DisplayName("게시판 직업 정보로 조회")
    void findByJobs() throws Exception{
        // given
        List<Long> jobIds = setUpIds();

        List<BoardFindResponseDto> boardFindResponseDtos = setUpBoardFindResponseDto();
        when(boardRetrieveService.findByJobs(page, jobIds)).thenReturn(boardFindResponseDtos);

        // when, then
        mockMvc.perform(get(requestPrefix+"/jobs")
                        .param("jobIds", jobIds.stream().map(String::valueOf).toArray(String[]::new)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(boardFindResponseDtos)));
    }

    @Test
    @DisplayName("게시판 지역 - 구 정보로 조회")
    void findBySubLocations() throws Exception{
        // given
        List<Long> subLocationIds = setUpIds();

        List<BoardFindResponseDto> boardFindResponseDtos = setUpBoardFindResponseDto();
        when(boardRetrieveService.findBySubLocations(page, subLocationIds)).thenReturn(boardFindResponseDtos);

        // when, then
        mockMvc.perform(get(requestPrefix+"/subLocations")
                        .param("subLocationIds", subLocationIds.stream().map(String::valueOf).toArray(String[]::new)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(boardFindResponseDtos)));
    }

    @Test
    @DisplayName("게시판 지역 - 시 정보로 조회")
    void findByMainLocation() throws Exception{
        // given
        Long mainLocationId = 1L;

        List<BoardFindResponseDto> boardFindResponseDtos = setUpBoardFindResponseDto();
        when(boardRetrieveService.findByMainLocation(page, mainLocationId)).thenReturn(boardFindResponseDtos);

        // when, then
        mockMvc.perform(get(requestPrefix+"/mainLocation")
                        .param("mainLocationId", mainLocationId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(boardFindResponseDtos)));
    }
    private List<BoardFindResponseDto> setUpBoardFindResponseDto(){
        List<BoardFindResponseDto> boardFindResponseDto = new ArrayList<>();
        for(int i = 0; i<10; i++){
            BoardFindResponseDto build = BoardFindResponseDto.builder()
                    .content("testC")
                    .title("testT")
                    .payType("testP")
                    .payment(100)
                    .endAt(new Date())
                    .startAt(new Date())
                    .recruitType("testR")
                    .companyLocation("testCL")
                    .cars(setUpJoinColumns("test car"))
                    .jobs(setUpJoinColumns("test job"))
                    .locations(setUpJoinColumns("test location"))
                    .build();
            boardFindResponseDto.add(build);
        }
        return boardFindResponseDto;
    }

    private List<String> setUpJoinColumns(String value){
        List<String> result = new ArrayList<>();
        for (int i =0; i<10; i++) {
            result.add(value + i);
        }
        return result;
    }
    private List<Long> setUpIds(){
        List<Long> ids = new ArrayList<>();
        for(int i =0; i<10; i++){
            ids.add((long) i);
        }

        return ids;
    }

}