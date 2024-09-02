package com.alldriver.alldriver.community.service.impl;

import com.alldriver.alldriver.board.domain.MainLocation;
import com.alldriver.alldriver.board.domain.SubLocation;
import com.alldriver.alldriver.board.repository.SubLocationRepository;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.dto.request.CommunitySaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunitySaveResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityUpdateResponseDto;
import com.alldriver.alldriver.community.repository.CommunityRepository;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommunityServiceImplTest {

    @InjectMocks
    private CommunityServiceImpl communityService;
    @Mock
    private CommunityRepository communityRepository;
    @Mock
    private SubLocationRepository subLocationRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("저장 성공")
    void save() {
        // given
        SubLocation subLocation = setUpSubLocation();
        User user = setUpUser();
        Community community = setUpCommunity();
        CommunitySaveRequestDto communitySaveRequestDto = setUpRequestDto();
        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(user));
            when(subLocationRepository.findById(any())).thenReturn(Optional.ofNullable(subLocation));
            when(communityRepository.save(any())).thenReturn(community);
            // when
            CommunitySaveResponseDto save = communityService.save(communitySaveRequestDto);

            // then
            assertThat(save.getId()).isEqualTo(community.getId());
            assertThat(save.getContent()).isEqualTo(community.getContent());
            assertThat(save.getTitle()).isEqualTo(community.getTitle());
            assertThat(save.getCreatedAt()).isEqualTo(community.getCreatedAt());
        }
    }

    @Test
    @DisplayName("저장 실패 - 유저 미존재")
    void saveFailWhenUserNotFound() {
        // given

        CommunitySaveRequestDto communitySaveRequestDto = setUpRequestDto();
        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(userRepository.findByUserId(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

            // when
            CustomException customException = assertThrows(CustomException.class, () -> communityService.save(communitySaveRequestDto));

            // then
            assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND);
        }
    }
    @Test
    @DisplayName("저장 실패 - 지역 미존재")
    void saveFailWhenSubLocationNotFound() {
        // given
        User user = setUpUser();
        CommunitySaveRequestDto communitySaveRequestDto = setUpRequestDto();
        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(user));
            when(subLocationRepository.findById(any())).thenThrow(new CustomException(ErrorCode.SUB_LOCATION_NOT_FOUND));

            // when
            CustomException customException = assertThrows(CustomException.class, () -> communityService.save(communitySaveRequestDto));


            // then
            assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.SUB_LOCATION_NOT_FOUND);
        }
    }
    @Test
    @DisplayName("업데이트 성공")
    void update() {
        // given
        Community community = setUpCommunity();
        when(communityRepository.findById(any())).thenReturn(Optional.ofNullable(community));
        when(communityRepository.save(any())).thenReturn(setUpUpdatedCommunity());
        CommunityUpdateRequestDto communityUpdateRequestDto = setUpUpdateRequestDto();

        // when
        CommunityUpdateResponseDto update = communityService.update(communityUpdateRequestDto);

        // then
        assertThat(update.getId()).isEqualTo(communityUpdateRequestDto.getId());
        assertThat(update.getContent()).isEqualTo(communityUpdateRequestDto.getContent());
        assertThat(update.getTitle()).isEqualTo(communityUpdateRequestDto.getTitle());

    }
    @Test
    @DisplayName("업데이트 실패 - 커뮤니티 미존재")
    void updateWhenCommunityNotFound() {
        // given
        when(communityRepository.findById(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));
        CommunityUpdateRequestDto communityUpdateRequestDto = setUpUpdateRequestDto();

        // when
        CustomException customException = assertThrows(CustomException.class, () -> communityService.update(communityUpdateRequestDto));


        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.COMMUNITY_NOT_FOUND);

    }

    @Test
    @DisplayName("삭제 성공")
    void delete() {
        // given
        Long communityId = 1L;
        Community community = setUpCommunity();
        when(communityRepository.findById(any())).thenReturn(Optional.ofNullable(community));
        when(communityRepository.save(any())).thenReturn(setUpDeletedCommunity());
        // when
        CommunityDeleteResponseDto delete = communityService.delete(communityId);

        // then
        assertThat(delete.getId()).isEqualTo(communityId);
    }
    @Test
    @DisplayName("삭제 실패 - 커뮤니티 미존재")
    void deleteFailWhenCommunityNotFound() {
        // given
        Long wrongId = 2L;
        when(communityRepository.findById(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));

        // when
        CustomException customException = assertThrows(CustomException.class, () -> communityService.delete(wrongId));


        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.COMMUNITY_NOT_FOUND);

    }
    private CommunitySaveRequestDto setUpRequestDto(){
        return CommunitySaveRequestDto.builder()

                .content("title")
                .title("title")
                .build();
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

    private SubLocation setUpSubLocation(){
        return SubLocation.builder()
                .category("test" + 1)
                .mainLocation(new MainLocation())
                .build();

    }

    private Community setUpCommunity(){
        return Community.builder()
                .id(1L)
                .title("title")
                .createdAt(LocalDateTime.now())
                .content("content")
                .deleted(false)
                .user(new User())

                .build();
    }
    private CommunityUpdateRequestDto setUpUpdateRequestDto(){
        return CommunityUpdateRequestDto.builder()
                .title("modified")
                .content("modified")
                .id(1L)
                .build();
    }
    private Community setUpUpdatedCommunity(){
        return Community.builder()
                .id(1L)
                .title("modified")
                .createdAt(LocalDateTime.now())
                .content("modified")
                .deleted(false)
                .user(new User())

                .build();
    }
    private Community setUpDeletedCommunity(){
        return Community.builder()
                .id(1L)
                .title("modified")
                .createdAt(LocalDateTime.now())
                .content("modified")
                .deleted(true)
                .user(new User())

                .build();
    }
}