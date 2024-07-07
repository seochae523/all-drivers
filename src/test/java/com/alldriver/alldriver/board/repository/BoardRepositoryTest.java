package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.*;
import com.alldriver.alldriver.board.vo.BoardFindVo;
import com.alldriver.alldriver.common.emun.Role;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


import java.time.LocalDateTime;
import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SubLocationRepository subLocationRepository;
    String userId = "testUser";
    Integer offset = 0;
    Integer limit = 10;
    @BeforeEach
    void init(){
        User user = setUpUser();
        Board board = setUpBoard(user);

        boardRepository.save(board);
        this.setUpJoinTables(user);
    }

    @Test
    @DisplayName("전체 조회")
    void 전체_조회() {
        // given
        // when
        List<BoardFindVo> boardPage = boardRepository.findAll(limit, offset, userId);

        // then
        assertThat(boardPage).hasSize(10);
    }
    @Test
    @DisplayName("차량으로 조회 성공")
    void 차량으로_조회_성공(){
        // given
        List<Long> carIds = new ArrayList<>();
        carIds.add(1L);

        // when
        List<BoardFindVo> byCars = boardRepository.findByCars(limit, offset, carIds, userId);
        // then
        assertThat(byCars).hasSize(10);
    }
    @Test
    @DisplayName("차량으로 조회 실패")
    void 차량으로_조회_실패(){
        // given
        List<Long> carIds = new ArrayList<>();
        carIds.add(3L);

        // when
        List<BoardFindVo> byCars = boardRepository.findByCars(limit, offset, carIds, userId);
        // then
        assertThat(byCars).hasSize(0);
    }

    @Test
    @DisplayName("직업으로 조회 성공")
    void 직업으로_조회_성공(){
        // given
        List<Long> jobIds = new ArrayList<>();
        jobIds.add(1L);

        // when
        List<BoardFindVo> byCars = boardRepository.findByJobs(limit, offset, jobIds, userId);
        // then
        assertThat(byCars).hasSize(10);
    }

    @Test
    @DisplayName("직업으로 조회 실패")
    void 직업으로_조회_실패(){
        // given
        List<Long> jobIds = new ArrayList<>();
        jobIds.add(0L);
        Pageable pageable = PageRequest.of(0, 10);
        // when
        List<BoardFindVo> byCars = boardRepository.findByJobs(limit, offset, jobIds, userId);
        // then
        assertThat(byCars).hasSize(0);
    }
    @Test
    @DisplayName("지역으로 조회 성공")
    void 지역으로_조회_성공(){
        // given
        List<Long> subLocationIds = new ArrayList<>();
        subLocationIds.add(100L);

        // when
        List<BoardFindVo> bySubLocations= boardRepository.findBySubLocations(limit, offset, subLocationIds, userId);
        // then
        assertThat(bySubLocations).hasSize(10);
    }

    @Test
    @DisplayName("지역으로 조회 실패")
    void 지역으로_조회_실패(){
        // given
        List<Long> subLocationIds = new ArrayList<>();
        subLocationIds.add(0L);

        // when
        List<BoardFindVo> bySubLocations= boardRepository.findBySubLocations(limit, offset, subLocationIds, userId);
        // then
        assertThat(bySubLocations).hasSize(0);
    }

    @Test
    @DisplayName("시로 조회 성공")
    void 시로_조회_성공(){
        // given
        Long mainLocationId = 6L;

        // when
        List<BoardFindVo> byMainLocation = boardRepository.findByMainLocation(limit, offset, mainLocationId, userId);
        // then
        assertThat(byMainLocation).hasSize(10);
    }

    @Test
    @DisplayName("시로 조회 실패")
    void 시로_조회_실패(){
        // given
        Long mainLocationId = 10L;

        // when
        List<BoardFindVo> byMainLocation = boardRepository.findByMainLocation(limit, offset, mainLocationId, userId);
        // then
        assertThat(byMainLocation).hasSize(0);
    }

    private User setUpUser(){
        return User.builder()
                .userId("testUser")
                .name("testName")
                .deleted(false)
                .nickname("testNick")
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

            Car car = carRepository.findById(1L).get();
            Job job = jobRepository.findById(1L).get();
            // sub location 100의 main location id = 6
            SubLocation subLocation = subLocationRepository.findById(100L).get();
            Board build = Board.builder()
                    .content("testContent" + i)
                    .title("testT" + i)
                    .payType("" + i)
                    .payment(100)
                    .recruitType("testR")
                    .companyLocation("test comp loc")
                    .createdAt(LocalDateTime.now())
                    .startAt(new Date())
                    .endAt(new Date())
                    .deleted(false)
                    .user(user)
                    .build();
            CarBoard carBoard = CarBoard.builder().board(build).car(car).build();
            JobBoard jobBoard = JobBoard.builder().board(build).job(job).build();
            LocationBoard locationBoard = LocationBoard.builder().board(build).subLocation(subLocation).build();
            build.addCarBoard(carBoard);
            build.addJobBoard(jobBoard);
            build.addLocationBoard(locationBoard);

            boardRepository.save(build);
        }
    }
}