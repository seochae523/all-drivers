package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.domain.MainLocation;
import com.alldriver.alldriver.board.dto.request.BoardSaveRequestDto;
import com.alldriver.alldriver.board.dto.response.BoardSaveResponseDto;
import com.alldriver.alldriver.board.repository.*;
import com.alldriver.alldriver.board.service.impl.BoardServiceImpl;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;


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
        BoardSaveRequestDto boardSaveRequestDto = setUpBoardSaveRequestDto();
        when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(new User()));
        when(mainLocationRepository.findById(1L)).thenReturn(Optional.ofNullable(new MainLocation()));
        when(boardRepository.save(any())).thenReturn(boardSaveRequestDto.toEntity(new User()));
        // when
        BoardSaveResponseDto save = boardService.save(new ArrayList<>(), boardSaveRequestDto);

        // then
        assertThat(save.getTitle()).isEqualTo(boardSaveRequestDto.getTitle());
        assertThat(save.getContent()).isEqualTo(boardSaveRequestDto.getContent());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
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
                .content("testC")
                .title("testT")
                .user(new User())
                .payType("testP")
                .payment(100)
                .endAt(new Date())
                .startAt(new Date())
                .recruitType("testR")
                .companyLocation("testCL")
                .deleted(false)
                .carBoards(new HashSet<>())
                .jobBoards(new HashSet<>())
                .locationBoards(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .build();
    }
}