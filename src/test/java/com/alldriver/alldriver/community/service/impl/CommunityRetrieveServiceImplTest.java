package com.alldriver.alldriver.community.service.impl;

import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.vo.BoardFindVo;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.community.dto.response.CommunityFindResponseDto;
import com.alldriver.alldriver.community.repository.CommunityRepository;
import com.alldriver.alldriver.community.vo.CommunityFindVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityRetrieveServiceImplTest {

    @InjectMocks
    private CommunityRetrieveServiceImpl communityRetrieveService;

    @Mock
    private CommunityRepository communityRepository;

    private final Integer pageSize = 10;
    private final Integer offset = 0;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(communityRetrieveService, "pageSize", pageSize);
    }

    @Test
    @DisplayName("전체 조회")
    void findAll() {
        CommunityFindVo communityFindVo = mock(CommunityFindVo.class);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            String userId = JwtUtils.getUserId();

            when(communityRepository.findAll(pageSize, offset, userId)).thenReturn(Collections.singletonList(communityFindVo));

            // when
            List<CommunityFindResponseDto> result = communityRetrieveService.findAll(0);

            // then
            assertThat(result).isNotEmpty();

        }
    }

    @Test
    @DisplayName("사용자 이름에 따른 조회")
    void findByUserId() {
        CommunityFindVo communityFindVo = mock(CommunityFindVo.class);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            String userId = JwtUtils.getUserId();

            when(communityRepository.findByUserId(pageSize, offset, userId)).thenReturn(Collections.singletonList(communityFindVo));

            // when
            List<CommunityFindResponseDto> result = communityRetrieveService.findByUserId(0);

            // then
            assertThat(result).isNotEmpty();

        }
    }

    @Test
    @DisplayName("지역 명에 따른 조회")
    void findBySubLocationId() {
        CommunityFindVo communityFindVo = mock(CommunityFindVo.class);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            String userId = JwtUtils.getUserId();
            Long subLocationId = 1L;
            when(communityRepository.findBySubLocation(pageSize, offset, userId, subLocationId)).thenReturn(Collections.singletonList(communityFindVo));

            // when
            List<CommunityFindResponseDto> result = communityRetrieveService.findBySubLocationId(0, subLocationId);

            // then
            assertThat(result).isNotEmpty();

        }
    }
}