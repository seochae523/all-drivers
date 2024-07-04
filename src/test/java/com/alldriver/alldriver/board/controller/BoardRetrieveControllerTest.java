package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.dto.response.BoardCarFindResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardJobFindResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardLocationFindResponseDto;
import com.alldriver.alldriver.board.service.BoardCategoryRetrieveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(BoardCategoryRetrieveController.class)
@WithMockUser("test")
class BoardRetrieveControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    BoardCategoryRetrieveService boardCategoryRetrieveService;
    @Test
    @DisplayName("전체 조회")
    void findAllBoards() {
    }

    @Test
    void findByCars() {
    }

    @Test
    void findByJobs() {
    }

    @Test
    void findBySubLocations() {
    }

    @Test
    void findByMainLocation() {
    }
    private List<BoardFindResponseDto> setUpBoard(String mainLocation){
        List<BoardFindResponseDto> boardFindResponseDto = new ArrayList<>();
        for(int i = 0; i<10; i++){
            BoardFindResponseDto.builder()
                    .content("testC")
                    .title("testT")
                    .payType("testP")
                    .payment(100)
                    .endAt(new Date())
                    .startAt(new Date())
                    .recruitType("testR")
                    .companyLocation("testCL")
                    .cars(new ArrayList<>())
                    .jobs(new ArrayList<>())
                    .locations(new ArrayList<>())
                    .mainLocation(mainLocation)
                    .build();
        }
    }
    private List<BoardJobFindResponseDto> setUpJobs(){
        List<BoardJobFindResponseDto> result = new ArrayList<>();
        for (int i =0 ;i<10; i++) {
            result.add(new BoardJobFindResponseDto((long)i, "test"));
        }
        return result;
    }
    private List<BoardCarFindResponseDto> setUpCars(){
        List<BoardCarFindResponseDto> result = new ArrayList<>();
        for (int i =0 ;i<10; i++) {
            result.add(new BoardCarFindResponseDto((long)i, "test"));
        }
        return result;
    }
    private List<BoardLocationFindResponseDto> setUpLocations(){
        List<BoardLocationFindResponseDto> result = new ArrayList<>();
        for (int i =0 ;i<10; i++) {
            result.add(new BoardLocationFindResponseDto((long)i, "test"));
        }
        return result;
    }

    private Page<Board> setUpBoards(){
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when

    }
}