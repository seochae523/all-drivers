package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.Job;
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
@ActiveProfiles("test")
class JobRepositoryTest {
    @Autowired
    private JobRepository jobRepository;

    @Test
    @DisplayName("전체 조회")
    void 전체_조회(){
        // given, when
        List<Job> all = jobRepository.findAll();

        // then
        assertThat(all).isNotEmpty();
    }

    @Test
    @DisplayName("id로 조회 성공")
    void id로_조회_성공(){
        // given
        List<Long> ids = new ArrayList<>();

        for (int i = 1; i<10; i++) {
            ids.add((long) i);
        }

        // when
        List<Job> byIds = jobRepository.findByIds(ids);

        // then
        assertThat(byIds).hasSize(9);
    }

    @Test
    @DisplayName("id로 조회 실패")
    void id로_조회_실패(){
        // given
        List<Long> ids = new ArrayList<>();
        ids.add(1000L);

        // when
        List<Job> byIds = jobRepository.findByIds(ids);

        // then
        assertThat(byIds).isEmpty();
    }
}