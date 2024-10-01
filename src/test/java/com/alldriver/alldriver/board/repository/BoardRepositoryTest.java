package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.*;
import com.alldriver.alldriver.board.dto.query.BoardSearchCondition;
import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.common.configuration.QueryDslConfig;
import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardBookmarkRepository boardBookmarkRepository;

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void init(){
        User user = setUpUser();
        User save = userRepository.save(user);
        this.setUpJoinTables(save);
    }


    @Test
    @DisplayName("전체조회")
    void findAll() {
        // given
        String userId = "testUser";
        // when
        Page<BoardFindResponseDto> result = boardRepository.findAll(userId, PageRequest.of(0, 10));
        // then
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(100);
    }

    @Test
    @DisplayName("즐겨찾기 게시글 조회")
    void findMyBookmarkedBoard() {
        // given
        String userId = "testUser";
        // when
        Page<BoardFindResponseDto> result = boardRepository.findMyBookmarkedBoard(userId, PageRequest.of(0, 10));
        // then
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(100);
    }

    @Test
    @DisplayName("상세조회")
    void findDetailById() {
        // given
        Long boardId = 1L;
        // when
        Optional<Board> result = boardRepository.findDetailById(boardId);
        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(boardId);
    }

    @Test
    @DisplayName("사용자 아이디로 조회")
    void findByUserId() {
        // given
        String userId = "testUser";
        // when
        Page<BoardFindResponseDto> result = boardRepository.findByUserId(userId, PageRequest.of(0, 10));

        // then
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(100);
    }

    @Test
    @DisplayName("복합 파라미터 조회 - 모든 조건 존재")
    void findByComplexParameterWithAll(){
        // given
        BoardSearchCondition boardSearchCondition = BoardSearchCondition.builder()
                .mainLocationId(1L)
                .subLocationIds(List.of(1L))
                .carIds(List.of(1L))
                .jobIds(List.of(1L))
                .build();

        String userId = "testUser";
        // when
        Page<BoardFindResponseDto> search = boardRepository.search(boardSearchCondition, PageRequest.of(0, 10), userId);

        // then
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getTotalElements()).isEqualTo(100);
    }

    @Test
    @DisplayName("복합 파라미터 조회 - sub + main + car")
    void findByComplexParameterWithSubMainCar(){
        // given
        BoardSearchCondition boardSearchCondition = BoardSearchCondition.builder()
                .mainLocationId(1L)
                .subLocationIds(List.of(1L))
                .carIds(List.of(1L))
                .build();

        String userId = "testUser";
        // when
        Page<BoardFindResponseDto> search = boardRepository.search(boardSearchCondition, PageRequest.of(1, 10), userId);
        // then
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getTotalElements()).isEqualTo(100);
    }

    @Test
    @DisplayName("복합 파라미터 조회 - sub + main + job")
    void findByComplexParameterWithSubMainJob(){
        // given
        BoardSearchCondition boardSearchCondition = BoardSearchCondition.builder()
                .mainLocationId(1L)
                .subLocationIds(List.of(1L))
                .jobIds(List.of(1L))
                .build();

        String userId = "testUser";
        // when
        // PageableExecutionUtils로 인한 다음 페이지 조회
        Page<BoardFindResponseDto> search = boardRepository.search(boardSearchCondition, PageRequest.of(1, 10), userId);
        // then
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getTotalElements()).isEqualTo(100);
    }

    @Test
    @DisplayName("복합 파라미터 조회 - sub + main")
    void findByComplexParameterWithSubMain(){
        // given
        BoardSearchCondition boardSearchCondition = BoardSearchCondition.builder()
                .mainLocationId(1L)
                .subLocationIds(List.of(1L))
                .build();

        String userId = "testUser";
        // when
        // PageableExecutionUtils로 인한 다음 페이지 조회
        Page<BoardFindResponseDto> search = boardRepository.search(boardSearchCondition, PageRequest.of(1, 10), userId);
        // then
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getTotalElements()).isEqualTo(100);
    }
    @Test
    @DisplayName("복합 파라미터 조회 - job + car")
    void findByComplexParameterWithJobCar(){
        // given
        BoardSearchCondition boardSearchCondition = BoardSearchCondition.builder()
                .jobIds(List.of(1L))
                .carIds(List.of(1L))
                .build();

        String userId = "testUser";
        // when
        // PageableExecutionUtils로 인한 다음 페이지 조회
        Page<BoardFindResponseDto> search = boardRepository.search(boardSearchCondition, PageRequest.of(1, 10), userId);
        // then
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getTotalElements()).isEqualTo(100);
    }
    @Test
    @DisplayName("복합 파라미터 조회 - job")
    void findByComplexParameterWithJob(){
        // given
        BoardSearchCondition boardSearchCondition = BoardSearchCondition.builder()
                .jobIds(List.of(1L))
                .build();

        String userId = "testUser";
        // when
        // PageableExecutionUtils로 인한 다음 페이지 조회
        Page<BoardFindResponseDto> search = boardRepository.search(boardSearchCondition, PageRequest.of(1, 10), userId);

        // then
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getTotalElements()).isEqualTo(100);
    }
    @Test
    @DisplayName("복합 파라미터 조회 - car")
    void findByComplexParameterWithCar(){
        // given
        BoardSearchCondition boardSearchCondition = BoardSearchCondition.builder()
                .carIds(List.of(1L))
                .build();

        String userId = "testUser";
        // when
        // PageableExecutionUtils로 인한 다음 페이지 조회
        Page<BoardFindResponseDto> search = boardRepository.search(boardSearchCondition, PageRequest.of(1, 10), userId);
        // then
        assertThat(search.getSize()).isEqualTo(10);

        assertThat(search.getTotalElements()).isEqualTo(100);
    }
    @Test
    @DisplayName("복합 파라미터 조회 - sub")
    void findByComplexParameterWithSub(){
        // given
        BoardSearchCondition boardSearchCondition = BoardSearchCondition.builder()
                .subLocationIds(List.of(1L))
                .build();

        String userId = "testUser";
        // when
        // PageableExecutionUtils로 인한 다음 페이지 조회
        Page<BoardFindResponseDto> search = boardRepository.search(boardSearchCondition, PageRequest.of(1, 10), userId);
        // then
        assertThat(search.getSize()).isEqualTo(10);
        assertThat(search.getTotalElements()).isEqualTo(100);
    }
    private User setUpUser(){
        return User.builder()
                .userId("testUser")
                .name("testName")
                .deleted(false)
                .password("testPassword")
                .role(Role.USER.getValue())
                .phoneNumber("01012345678")
                .createdAt(LocalDateTime.now())
                .refreshToken("testRefresh")
                .build();
    }
    private Board setUpBoard(User user){
        return Board.builder()
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
                .user(user)
                .build();
    }

    private void setUpJoinTables(User user){
        for (int i =0; i<100 ; i++) {
            Board build = setUpBoard(user);

            Car car = Car.builder().id(1L).build();
            Job job = Job.builder().id(1L).build();
            SubLocation subLocation = SubLocation.builder().id(1L).mainLocation(MainLocation.builder().id(1L).build()).build();

            CarBoard carBoard = CarBoard.builder().board(build).car(car).build();
            JobBoard jobBoard = JobBoard.builder().board(build).job(job).build();
            LocationBoard locationBoard = LocationBoard.builder().board(build).subLocation(subLocation).build();
            build.addCarBoard(carBoard);
            build.addJobBoard(jobBoard);
            build.addLocationBoard(locationBoard);

            Board save = boardRepository.save(build);
            BoardBookmark bookmark = BoardBookmark.builder().board(save).user(user).build();
            boardBookmarkRepository.save(bookmark);
        }
    }
}