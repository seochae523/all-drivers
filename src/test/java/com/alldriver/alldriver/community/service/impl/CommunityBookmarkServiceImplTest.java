package com.alldriver.alldriver.community.service.impl;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.domain.CommunityBookmark;
import com.alldriver.alldriver.community.dto.response.CommunityBookmarkDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityBookmarkSaveResponseDto;
import com.alldriver.alldriver.community.repository.CommunityBookmarkRepository;
import com.alldriver.alldriver.community.repository.CommunityRepository;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommunityBookmarkServiceImplTest {

    @InjectMocks
    private CommunityBookmarkServiceImpl communityBookmarkService;

    @Mock
    private CommunityBookmarkRepository communityBookmarkRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommunityRepository communityRepository;

    @Test
    @DisplayName("저장")
    void save() {
        // given
        User user = setUpUser();
        Community community = setUpCommunity(user);
        CommunityBookmark communityBookmark = CommunityBookmark.builder()
                .id(1L)
                .community(community)
                .user(user)
                .build();
        when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(user));
        when(communityRepository.findById(any())).thenReturn(Optional.ofNullable(community));
        when(communityBookmarkRepository.save(any())).thenReturn(communityBookmark);
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)) {
            // when

            CommunityBookmarkSaveResponseDto save = communityBookmarkService.save(1L);
            // then
            assertThat(save.getCommunityId()).isEqualTo(1L);
            assertThat(save.getUserId()).isEqualTo("testUser");
            assertThat(save.getBookmarkId()).isEqualTo(1L);
        }
    }

    @Test
    @DisplayName("삭제 성공")
    void delete() {
        // given
        Long communityId = 1L;
        User user = setUpUser();
        Community community = setUpCommunity(user);
        CommunityBookmark communityBookmark = CommunityBookmark.builder()
                .id(1L)
                .community(community)
                .user(user)
                .build();
        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            when(communityBookmarkRepository.findByCommunityIdAndUserId(any(), any())).thenReturn(Optional.ofNullable(communityBookmark));

            // when
            CommunityBookmarkDeleteResponseDto delete = communityBookmarkService.delete(communityId);
            // then
            assertThat(delete.getId()).isEqualTo(1L);
        }
    }
    @Test
    @DisplayName("삭제 실패")
    void deleteFail() {
        // given
        Long communityId = 1L;

        try (MockedStatic<JwtUtils> jwtUtils =mockStatic(JwtUtils.class)){
            when(communityBookmarkRepository.findByCommunityIdAndUserId(any(), any()))
                    .thenThrow(new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

            // when
            CustomException customException = assertThrows(CustomException.class, () -> communityBookmarkService.delete(communityId));

            // then
            assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.BOOKMARK_NOT_FOUND);
        }
    }
    private User setUpUser(){
        return User.builder()
                .userId("testUser")
                .name("testName")
                .deleted(false)
                .nickname("testNickname")
                .password("testPassword")
                .role(Role.USER.getValue())
                .phoneNumber("01012345678")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Community setUpCommunity(User user){
        return Community.builder()
                .id(1L)
                .title("test title")
                .content("test content")
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }
}