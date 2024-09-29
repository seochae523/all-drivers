package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.domain.Job;
import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.response.JobFindResponseDto;
import com.alldriver.alldriver.board.repository.BoardRepository;
import com.alldriver.alldriver.board.service.impl.BoardRetrieveServiceImpl;
import com.alldriver.alldriver.board.service.impl.BoardServiceImpl;
import com.alldriver.alldriver.board.vo.BoardFindVo;
import com.alldriver.alldriver.common.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardRetrieveServiceTest {

    @InjectMocks
    BoardRetrieveServiceImpl boardRetrieveService;
    @Mock
    BoardRepository boardRepository;

    final Integer pageSize = 10;
    final Integer offset = 0;
    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(boardRetrieveService, "pageSize", pageSize);
    }
    @Test
    @DisplayName("게시판 전체 조회")
    void findAll() {
        // given
        BoardFindVo boardFindVo = mock(BoardFindVo.class);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            String userId = JwtUtils.getUserId();

            when(boardRepository.findAll(pageSize, offset, userId)).thenReturn(Collections.singletonList(boardFindVo));

            // when
            List<BoardFindResponseDto> result = boardRetrieveService.findAll(0);

            // then
            assertThat(result).isNotEmpty();

        }
    }

    @Test
    @DisplayName("차량 id로 조회")
    void findByCars() {
        // given
        BoardFindVo boardFindVo = mock(BoardFindVo.class);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            String userId = JwtUtils.getUserId();
            List<Long> carIds = setUpIds();
            when(boardRepository.findByCars(carIds, userId)).thenReturn(Collections.singletonList(boardFindVo));

            // when
            List<BoardFindResponseDto> result = boardRetrieveService.findByCars(0, carIds);

            // then
            assertThat(result).isNotEmpty();

        }
    }

    @Test
    @DisplayName("직종 id로 조회")
    void findByJobs() {
        // given
        BoardFindVo boardFindVo = mock(BoardFindVo.class);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            String userId = JwtUtils.getUserId();
            List<Long> jobIds = setUpIds();
            when(boardRepository.findByJobs(pageSize, offset, jobIds, userId)).thenReturn(Collections.singletonList(boardFindVo));

            // when
            List<BoardFindResponseDto> result = boardRetrieveService.findByJobs(0, jobIds);

            // then
            assertThat(result).isNotEmpty();

        }
    }

    @Test
    @DisplayName("지역 - 구 id로 조회")
    void findBySubLocations() {
        // given
        BoardFindVo boardFindVo = mock(BoardFindVo.class);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            String userId = JwtUtils.getUserId();
            List<Long> locationIds = setUpIds();
            when(boardRepository.findBySubLocations(pageSize, offset,locationIds, userId)).thenReturn(Collections.singletonList(boardFindVo));

            // when
            List<BoardFindResponseDto> result = boardRetrieveService.findBySubLocations(0, locationIds);

            // then
            assertThat(result).isNotEmpty();

        }
    }

    @Test
    @DisplayName("지역 - 시 id로 조회")
    void findByMainLocation() {
        // given
        BoardFindVo boardFindVo = mock(BoardFindVo.class);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            String userId = JwtUtils.getUserId();
            Long mainLocationId = 1L;
            when(boardRepository.findByMainLocation(pageSize, offset,mainLocationId, userId)).thenReturn(Collections.singletonList(boardFindVo));

            // when
            List<BoardFindResponseDto> result = boardRetrieveService.findByMainLocation(0, mainLocationId);

            // then
            assertThat(result).isNotEmpty();

        }
    }

    @Test
    @DisplayName("사용자 id로 조회")
    void findByUserId() {
        // given
        BoardFindVo boardFindVo = mock(BoardFindVo.class);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            String userId = JwtUtils.getUserId();

            when(boardRepository.findByUserId(pageSize, offset,userId)).thenReturn(Collections.singletonList(boardFindVo));

            // when
            List<BoardFindResponseDto> result = boardRetrieveService.findByUserId(0);


            // then
            assertThat(result).isNotEmpty();

        }
    }

    @Test
    @DisplayName("검색")
    void search() {
        // given
        BoardFindVo boardFindVo = mock(BoardFindVo.class);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)) {
            String userId = JwtUtils.getUserId();
            String keyword = "hello";
            when(boardRepository.search(keyword,userId)).thenReturn(Collections.singletonList(boardFindVo));

            // when
            List<BoardFindResponseDto> result = boardRetrieveService.search(0, keyword);


            // then
            assertThat(result).isNotEmpty();
        }
    }

    private List<Long> setUpIds(){
        List<Long> ids = new ArrayList<>();
        for(int i =1; i<11; i++){
            ids.add((long)i);
        }
        return ids;
    }

}