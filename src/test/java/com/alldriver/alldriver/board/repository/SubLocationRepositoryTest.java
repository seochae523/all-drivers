package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.MainLocation;
import com.alldriver.alldriver.board.domain.SubLocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class SubLocationRepositoryTest {

    @Autowired
    private SubLocationRepository subLocationRepository;

    @Test
    @DisplayName("전체 조회")
    void 전체_조회(){
        // given, when
        List<SubLocation> all = subLocationRepository.findAll();

        // then
        assertThat(all).isNotEmpty();
    }

    @Test
    @DisplayName("시 id로 조회 성공")
    void 시_id로_조회_성공(){
        // given
        Long mainLocationId = 1L;

        // when
        List<SubLocation> byMainLocation = subLocationRepository.findByMainLocation(mainLocationId);

        // then
        assertThat(byMainLocation).isNotEmpty();

    }

    @Test
    @DisplayName("시 id로 조회 실패")
    void 시_id로_조회_실패(){
        // given
        Long mainLocationId = 0L;

        // when
        List<SubLocation> byMainLocation = subLocationRepository.findByMainLocation(mainLocationId);

        // then
        assertThat(byMainLocation).isEmpty();
    }

    @Test
    @DisplayName("id로 조회 성공")
    void id로_조회_성공(){
        // given
        List<Long> locationIds = new ArrayList<>();
        locationIds.add(1L);

        // when
        List<SubLocation> byIds = subLocationRepository.findByIds(locationIds);

        // then
        assertThat(byIds).hasSize(1);
    }

    @Test
    @DisplayName("id로 조회 실패")
    void id로_조회_실패(){
        // given
        List<Long> locationIds = new ArrayList<>();
        locationIds.add(0L);

        // when
        List<SubLocation> byIds = subLocationRepository.findByIds(locationIds);

        // then
        assertThat(byIds).isEmpty();
    }
}