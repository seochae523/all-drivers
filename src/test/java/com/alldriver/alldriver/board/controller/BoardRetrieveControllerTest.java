package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.dto.response.*;
import com.alldriver.alldriver.board.service.impl.BoardRetrieveServiceImpl;
import com.alldriver.alldriver.board.service.impl.BoardServiceImpl;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardRetrieveController.class)
@WithMockUser("test")
class BoardRetrieveControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    BoardRetrieveServiceImpl boardRetrieveService;

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("게시글 전체 조회")
    void findAll() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = setUpResponse();
        when(boardRetrieveService.findAll(0)).thenReturn(response);

        // when, then
        mockMvc.perform(get("/user/board/all/0").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.currentPage").value(0));

    }

    @Test
    @DisplayName("게시글 전체 조회 - 빈 리스트")
    void findAllEmpty() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = new PagingResponseDto<>(0, 0L, 0, new ArrayList<>());
        when(boardRetrieveService.findAll(0)).thenReturn(response);

        // when, then
        mockMvc.perform(get("/user/board/all/0").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.currentPage").value(0));

    }

    @Test
    @DisplayName("게시글 상세 조회")
    void findDetailById() throws Exception {
        // given
        BoardDetailResponseDto response = setUpDetailResponse();
        when(boardRetrieveService.findDetailById(1L)).thenReturn(response);
        List<CarFindResponseDto> cars = cars();
        List<JobFindResponseDto> jobs = jobs();
        // when, then
        mockMvc.perform(get("/user/board/all/detail/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.userId").value("testUser"))
                .andExpect(jsonPath("$.jobs").isArray())
                .andExpect(jsonPath("$.jobs[0].id").value(jobs.get(0).id()))
                .andExpect(jsonPath("$.cars").isArray())
                .andExpect(jsonPath("$.cars[0].id").value(cars.get(0).id()));


    }

    @Test
    @DisplayName("게시글 상세 조회 실패")
    void findDetailByIdFail() throws Exception {
        // given
        when(boardRetrieveService.findDetailById(1L)).thenThrow(new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // when, then
        mockMvc.perform(get("/user/board/all/detail/1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.BOARD_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BOARD_NOT_FOUND.getMessage()));

    }


    @Test
    @DisplayName("즐겨찾기 한 게시물 조회")
    void findByUserBookmark() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = setUpResponse();
        when(boardRetrieveService.findMyBookmarkedBoard(0)).thenReturn(response);

        // when, then
        mockMvc.perform(get("/user/board/bookmark/0").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.currentPage").value(0));

    }
    @Test
    @DisplayName("즐겨찾기 한 게시물 조회 - 빈 리스트")
    void findByUserBookmarkEmpty() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = new PagingResponseDto<>(0, 0L, 0, new ArrayList<>());
        when(boardRetrieveService.findMyBookmarkedBoard(0)).thenReturn(response);

        // when, then
        mockMvc.perform(get("/user/board/bookmark/0").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.currentPage").value(0));

    }
    // es 테스트 따로하기
//    @Test
//    void search() {
//    }

    @Test
    @DisplayName("복합 파라미터 조회 - main, sub, car, job")
    void findByAllParameters() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = setUpResponse();
        when(boardRetrieveService.findBy(0, List.of(1L), List.of(1L), List.of(1L), 1L)).thenReturn(response);
        // when, then
        mockMvc.perform(get("/user/board/by/0").with(csrf())
                        .param("mainLocationId", "1")
                        .param("subLocationIds", "1")
                        .param("carIds", "1")
                        .param("jobIds", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.currentPage").value(0));

    }
    @Test
    @DisplayName("복합 파라미터 조회 - main, sub, car")
    void findByMainAndSubAndCar() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = setUpResponse();
        when(boardRetrieveService.findBy(0, null, List.of(1L), List.of(1L), 1L)).thenReturn(response);
        // when, then
        mockMvc.perform(get("/user/board/by/0").with(csrf())
                        .param("mainLocationId", "1")
                        .param("subLocationIds", "1")
                        .param("carIds", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.currentPage").value(0));

    }

    @Test
    @DisplayName("복합 파라미터 조회 - main, sub, job")
    void findByMainAndSubAndJob() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = setUpResponse();
        when(boardRetrieveService.findBy(0, List.of(1L), null, List.of(1L), 1L)).thenReturn(response);
        // when, then
        mockMvc.perform(get("/user/board/by/0").with(csrf())
                        .param("mainLocationId", "1")
                        .param("subLocationIds", "1")
                        .param("jobIds", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.currentPage").value(0));

    }
    @Test
    @DisplayName("복합 파라미터 조회 - main, sub")
    void findByMainAndSub() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = setUpResponse();
        when(boardRetrieveService.findBy(0, null, null, List.of(1L), 1L)).thenReturn(response);
        // when, then
        mockMvc.perform(get("/user/board/by/0").with(csrf())
                        .param("mainLocationId", "1")
                        .param("subLocationIds", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.currentPage").value(0));

    }

    @Test
    @DisplayName("복합 파라미터 조회 - main")
    void findByMain() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = setUpResponse();
        when(boardRetrieveService.findBy(0, null, null, null, 1L)).thenReturn(response);
        // when, then
        mockMvc.perform(get("/user/board/by/0").with(csrf())
                        .param("mainLocationId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.currentPage").value(0));

    }
    @Test
    @DisplayName("복합 파라미터 조회 - car, job")
    void findByCarAndJob() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = setUpResponse();
        when(boardRetrieveService.findBy(0, List.of(1L), List.of(1L), null, null)).thenReturn(response);
        // when, then
        mockMvc.perform(get("/user/board/by/0").with(csrf())
                        .param("carIds", "1")
                        .param("jobIds", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.currentPage").value(0));

    }

    @Test
    @DisplayName("복합 파라미터 조회 - car")
    void findByCar() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = setUpResponse();
        when(boardRetrieveService.findBy(0, null, List.of(1L), null, null)).thenReturn(response);
        // when, then
        mockMvc.perform(get("/user/board/by/0").with(csrf())
                        .param("carIds", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.currentPage").value(0));

    }
    @Test
    @DisplayName("복합 파라미터 조회 - jobs")
    void findByJob() throws Exception {
        // given
        PagingResponseDto<List<BoardFindResponseDto>> response = setUpResponse();
        when(boardRetrieveService.findBy(0, List.of(1L), null, null, null)).thenReturn(response);
        // when, then
        mockMvc.perform(get("/user/board/by/0").with(csrf())
                        .param("jobIds", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.totalPage").value(0))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.currentPage").value(0));

    }

    private PagingResponseDto<List<BoardFindResponseDto>> setUpResponse(){
        return PagingResponseDto.<List<BoardFindResponseDto>>builder()
                .currentPage(0)
                .totalElements(10L)
                .data(setUpBoardFindResponseDto().getContent())
                .totalPage(0)
                .build();
    }
    private Page<BoardFindResponseDto> setUpBoardFindResponseDto(){
        List<BoardFindResponseDto> result = new ArrayList<>();
        for(int i = 0; i<10; i++) {
            BoardFindResponseDto build = BoardFindResponseDto.builder()
                    .id(1L+i)
                    .bookmarked(1)
                    .title("test")
                    .createdAt(LocalDateTime.now())
                    .userId("test")
                    .mainLocation(new MainLocationFindResponseDto(1L, "test"))
                    .subLocations(new ArrayList<>())
                    .build();

            result.add(build);
        }
        return new PageImpl<>(result, PageRequest.of(0, 10), 10);
    }
    private BoardDetailResponseDto setUpDetailResponse(){
        return BoardDetailResponseDto.builder()
                .id(1L)
                .boardImages(new ArrayList<>())
                .endAt(new Date())
                .startAt(new Date())
                .title("title")
                .content("content")
                .userId("testUser")
                .createdAt(LocalDateTime.now())
                .cars(cars())
                .jobs(jobs())
                .build();

    }
    private List<CarFindResponseDto> cars(){
        return List.of(new CarFindResponseDto(1L, "test1"), new CarFindResponseDto(2L, "test2"));
    }
    private List<JobFindResponseDto> jobs(){
        return List.of(new JobFindResponseDto(1L, "test1"), new JobFindResponseDto(2L, "test2"));
    }
}