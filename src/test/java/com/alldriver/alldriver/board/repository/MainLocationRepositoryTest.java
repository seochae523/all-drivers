package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.MainLocation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class MainLocationRepositoryTest {
    @Autowired
    private MainLocationRepository mainLocationRepository;

    @Test
    @DisplayName("전체 조회")
    void 전체_조회(){
        // given, when
        List<MainLocation> all = mainLocationRepository.findAll();

        // then
        assertThat(all).isNotEmpty();
    }

    @Test
    @DisplayName("시 이름으로 조회 성공")
    void 시_이름으로_조회_성공(){
        // given
        String category = "서울";
        // when
        Optional<MainLocation> byCategory = mainLocationRepository.findByCategory(category);
        // then

        assertThat(byCategory).isNotEmpty();
    }

    @Test
    @DisplayName("시 이름으로 조회 실패")
    void 시_이름으로_조회_실패(){
        // given
        String category = "뉴욕";
        // when
        Optional<MainLocation> byCategory = mainLocationRepository.findByCategory(category);
        // then

        assertThat(byCategory).isEmpty();
    }
}