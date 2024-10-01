package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.domain.LocationBoard;
import com.alldriver.alldriver.board.domain.SubLocation;
import com.alldriver.alldriver.board.dto.query.SubLocationQueryDto;
import com.alldriver.alldriver.board.dto.response.BoardDetailResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.response.MainLocationFindResponseDto;
import com.alldriver.alldriver.board.dto.response.PagingResponseDto;
import com.alldriver.alldriver.board.repository.BoardRepository;
import com.alldriver.alldriver.board.repository.LocationBoardRepository;
import com.alldriver.alldriver.board.service.impl.BoardRetrieveServiceImpl;
import com.alldriver.alldriver.board.service.impl.BoardServiceImpl;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.user.domain.User;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardRetrieveServiceTest {
    @InjectMocks
    private BoardRetrieveServiceImpl boardRetrieveService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private LocationBoardRepository locationBoardRepository;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(boardRetrieveService, "pageSize", 10);
    }

    @Test
    void findAll() {
        // given
        Page<BoardFindResponseDto> boardFindResponseDtos = setUpBoardFindResponseDto();
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)) {
            when(JwtUtils.getUserId()).thenReturn("test");
            when(boardRepository.findAll(any(String.class), any(Pageable.class))).thenReturn(boardFindResponseDtos);
            when(locationBoardRepository.findByBoardIds(any())).thenReturn(setUpSubLocationQueryDto());
            // when
            PagingResponseDto<List<BoardFindResponseDto>> all = boardRetrieveService.findAll(0);

            // then
            assertThat(all.getTotalElements()).isEqualTo(10);
            assertThat(all.getData().get(0).getId()).isEqualTo(1);
        }

    }

    @Test
    @DisplayName("디테일 조회 성공")
    void findDetailById() {
        // given
        Board board = setUpBoard();
        when(boardRepository.findDetailById(1L)).thenReturn(Optional.ofNullable(board));

        // when
        BoardDetailResponseDto result = boardRetrieveService.findDetailById(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
    }
    @Test
    @DisplayName("디테일 조회 실패")
    void findDetailByIdFail() {
        // given
        when(boardRepository.findDetailById(1L)).thenThrow(new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // when
        CustomException customException = assertThrows(CustomException.class, () -> boardRetrieveService.findDetailById(1L));

        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.BOARD_NOT_FOUND);
    }

    @Test
    @DisplayName("즐겨찾기 한 게시글 조회")
    void findMyBookmarkedBoard() {
        // given
        Page<BoardFindResponseDto> boardFindResponseDtos = setUpBoardFindResponseDto();
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)) {
            when(JwtUtils.getUserId()).thenReturn("test");
            when(boardRepository.findMyBookmarkedBoard(any(String.class), any(Pageable.class))).thenReturn(boardFindResponseDtos);
            when(locationBoardRepository.findByBoardIds(any())).thenReturn(setUpSubLocationQueryDto());
            // when
            PagingResponseDto<List<BoardFindResponseDto>> all = boardRetrieveService.findMyBookmarkedBoard(0);

            // then
            assertThat(all.getTotalElements()).isEqualTo(10);
            assertThat(all.getData().get(0).getId()).isEqualTo(1);
        }
    }

    @Test
    @DisplayName("복합파라미터 조회 : main + sub + car + job")
    void findByAllParameters() {
        // given
        Page<BoardFindResponseDto> result = setUpBoardFindResponseDto();
        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(boardRepository.search(any(), any(), any())).thenReturn(result);
            when(locationBoardRepository.findByBoardIds(any())).thenReturn(setUpSubLocationQueryDto());

            // when
            PagingResponseDto<List<BoardFindResponseDto>> all = boardRetrieveService.findBy(0, List.of(1L), List.of(1L), List.of(1L), 1L);

            // then
            assertThat(all.getTotalElements()).isEqualTo(10);
            assertThat(all.getData()).hasSize(10);
        }
    }
    @Test
    @DisplayName("복합파라미터 조회 : main + sub + car ")
    void findByMainSubCar() {
        // given
        Page<BoardFindResponseDto> result = setUpBoardFindResponseDto();
        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(boardRepository.search(any(), any(), any())).thenReturn(result);
            when(locationBoardRepository.findByBoardIds(any())).thenReturn(setUpSubLocationQueryDto());

            // when
            PagingResponseDto<List<BoardFindResponseDto>> all = boardRetrieveService.findBy(0, null, List.of(1L), List.of(1L), 1L);

            // then
            assertThat(all.getTotalElements()).isEqualTo(10);
            assertThat(all.getData()).hasSize(10);
        }
    }
    @Test
    @DisplayName("복합파라미터 조회 : main + sub + job")
    void findByMainSubJob() {
        // given
        Page<BoardFindResponseDto> result = setUpBoardFindResponseDto();
        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(boardRepository.search(any(), any(), any())).thenReturn(result);
            when(locationBoardRepository.findByBoardIds(any())).thenReturn(setUpSubLocationQueryDto());

            // when
            PagingResponseDto<List<BoardFindResponseDto>> all = boardRetrieveService.findBy(0, List.of(1L), null, List.of(1L), 1L);

            // then
            assertThat(all.getTotalElements()).isEqualTo(10);
            assertThat(all.getData()).hasSize(10);
        }
    }
    @Test
    @DisplayName("복합파라미터 조회 : main + sub ")
    void findByMainSub() {
        // given
        Page<BoardFindResponseDto> result = setUpBoardFindResponseDto();
        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(boardRepository.search(any(), any(), any())).thenReturn(result);
            when(locationBoardRepository.findByBoardIds(any())).thenReturn(setUpSubLocationQueryDto());

            // when
            PagingResponseDto<List<BoardFindResponseDto>> all = boardRetrieveService.findBy(0, null, null, List.of(1L), 1L);

            // then
            assertThat(all.getTotalElements()).isEqualTo(10);
            assertThat(all.getData()).hasSize(10);
        }
    }
    @Test
    @DisplayName("복합파라미터 조회 : main")
    void findByMain() {
        // given
        Page<BoardFindResponseDto> result = setUpBoardFindResponseDto();
        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(boardRepository.search(any(), any(), any())).thenReturn(result);
            when(locationBoardRepository.findByBoardIds(any())).thenReturn(setUpSubLocationQueryDto());

            // when
            PagingResponseDto<List<BoardFindResponseDto>> all = boardRetrieveService.findBy(0, null, null, null, 1L);

            // then
            assertThat(all.getTotalElements()).isEqualTo(10);
            assertThat(all.getData()).hasSize(10);
        }
    }

    @Test
    @DisplayName("복합파라미터 조회 : job")
    void findByJob() {
        // given
        Page<BoardFindResponseDto> result = setUpBoardFindResponseDto();
        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(boardRepository.search(any(), any(), any())).thenReturn(result);
            when(locationBoardRepository.findByBoardIds(any())).thenReturn(setUpSubLocationQueryDto());

            // when
            PagingResponseDto<List<BoardFindResponseDto>> all = boardRetrieveService.findBy(0, List.of(1L), null, null, null);

            // then
            assertThat(all.getTotalElements()).isEqualTo(10);
            assertThat(all.getData()).hasSize(10);
        }
    }

    @Test
    @DisplayName("복합파라미터 조회 : car")
    void findByCar() {
        // given
        Page<BoardFindResponseDto> result = setUpBoardFindResponseDto();
        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(boardRepository.search(any(), any(), any())).thenReturn(result);
            when(locationBoardRepository.findByBoardIds(any())).thenReturn(setUpSubLocationQueryDto());

            // when
            PagingResponseDto<List<BoardFindResponseDto>> all = boardRetrieveService.findBy(0, null, List.of(1L), null,null);

            // then
            assertThat(all.getTotalElements()).isEqualTo(10);
            assertThat(all.getData()).hasSize(10);
        }
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
    private List<SubLocationQueryDto> setUpSubLocationQueryDto() {
        List<SubLocationQueryDto> result = new ArrayList<>();
        for(int i = 0; i<10; i++) {
            SubLocationQueryDto build = new SubLocationQueryDto(i+1L, i+1L, "test", 1L, "test");
            result.add(build);
        }
        return result;
    }
    private Board setUpBoard() {
        return Board.builder()
                .id(1L)
                .content("testContent")
                .title("testTitle")
                .payType("testP")
                .payment(100)
                .recruitType("testR")
                .category("cate")
                .companyLocation("test comp loc")
                .createdAt(LocalDateTime.now())
                .startAt(new Date())
                .endAt(new Date())
                .deleted(false)
                .user(new User())
                .build();
    }

}