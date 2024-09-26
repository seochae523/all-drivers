package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.document.BoardDocument;
import com.alldriver.alldriver.board.domain.*;
import com.alldriver.alldriver.board.dto.request.*;
import com.alldriver.alldriver.board.dto.response.BoardSaveResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardUpdateResponseDto;
import com.alldriver.alldriver.board.repository.*;
import com.alldriver.alldriver.board.service.impl.BoardServiceImpl;
import static org.mockito.BDDMockito.given;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @InjectMocks
    private BoardServiceImpl boardService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private BoardSearchRepository boardSearchRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private BoardImageRepository boardImageRepository;
    @Mock
    private MainLocationRepository mainLocationRepository;
    @Mock
    private SubLocationRepository subLocationRepository;


    @Test
    @DisplayName("게시판 저장")
    void save() throws IOException {
        // given
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)) {
            BoardSaveRequestDto boardSaveRequestDto = setUpBoardSaveRequestDto();
            when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(new User()));
            when(mainLocationRepository.findById(1L)).thenReturn(Optional.ofNullable(new MainLocation()));
            when(boardRepository.save(any())).thenReturn(boardSaveRequestDto.toEntity(new User()));
            when(boardSearchRepository.save(any())).thenReturn(new BoardDocument());
            // when
            BoardSaveResponseDto save = boardService.save(new ArrayList<>(), boardSaveRequestDto);

            // then
            assertThat(save.getTitle()).isEqualTo(boardSaveRequestDto.getTitle());
            assertThat(save.getContent()).isEqualTo(boardSaveRequestDto.getContent());
        }
    }

    @Test
    @DisplayName("게시판 업데이트 성공")
    void update() throws IOException {

        // given
        Board board = setUpBoard();
        BoardUpdateRequestDto boardUpdateRequestDto= setUpBoardUpdateRequestDto();
        when(boardRepository.findById(any())).thenReturn(Optional.ofNullable(board));
        try(MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            given(JwtUtils.getUserId()).willReturn("test");
            when(carRepository.findById(any())).thenReturn(Optional.ofNullable(setUpCar()));
            when(jobRepository.findById(any())).thenReturn(Optional.ofNullable(setUpJob()));
            when(subLocationRepository.findById(any())).thenReturn(Optional.ofNullable(setUpSubLocation()));
            when(boardRepository.save(any())).thenReturn(board);
            // when
            BoardUpdateResponseDto update = boardService.update(null, boardUpdateRequestDto);

            assertThat(update.getId()).isEqualTo(1L);
            assertThat(update.getContent()).isEqualTo("updated");
            assertThat(update.getTitle()).isEqualTo("updated");
        }


    }

    @Test
    @DisplayName("게시판 삭제 성공")
    void delete() {
    }

    private BoardUpdateRequestDto setUpBoardUpdateRequestDto(){
        return BoardUpdateRequestDto.builder()
                .id(1L)
                .content("updated")
                .category("updated")
                .title("updated")
                .payment(123)
                .payType("주급")
                .recruitType("일용직")
                .companyLocation("updated")
                .startAt(new Date())
                .endAt(new Date())
                .mainLocationId(1L)
                .carInfos(new ArrayList<>(List.of(new CarUpdateRequestDto(0, 1L))))
                .jobInfos(new ArrayList<>(List.of(new JobUpdateRequestDto(0, 1L))))
                .locationInfos(new ArrayList<>(List.of(new LocationUpdateRequestDto(0, 1L))))
                .build();
    }
    private Car setUpCar(){
        return Car.builder().id(1L).build();
    }
    private Job setUpJob(){
        return Job.builder().id(1L).build();
    }
    private SubLocation setUpSubLocation(){
        return SubLocation.builder().id(1L).mainLocation(setUpMainLocation()).build();
    }
    private MainLocation setUpMainLocation(){
        return MainLocation.builder().id(1L).build();
    }

    private BoardSaveRequestDto setUpBoardSaveRequestDto(){
        return BoardSaveRequestDto.builder()
                .content("testC")
                .title("testT")
                .payType("testP")
                .payment(100)
                .endAt(new Date())
                .startAt(new Date())
                .recruitType("testR")
                .companyLocation("testCL")
                .car(new ArrayList<>())
                .job(new ArrayList<>())
                .subLocations(new ArrayList<>())
                .mainLocationId(1L)
                .build();
    }
    private Board setUpBoard(){
        return Board.builder()
                .id(1L)
                .content("testC")
                .title("testT")
                .user(User.builder().userId("test").build())
                .payType("testP")
                .payment(100)
                .endAt(new Date())
                .startAt(new Date())
                .recruitType("testR")
                .companyLocation("testCL")
                .category("cat")
                .deleted(false)
                .carBoards(new HashSet<>())
                .jobBoards(new HashSet<>())
                .locationBoards(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .build();
    }
}