package com.alldriver.alldriver.community.service.impl;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.domain.CommunityComment;
import com.alldriver.alldriver.community.dto.request.CommunityCommentSaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityCommentUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentFindResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentSaveResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentUpdateResponseDto;
import com.alldriver.alldriver.community.repository.CommunityCommentRepository;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * TODO : RESPONSE DTO 추가하기.
 */
@ExtendWith(MockitoExtension.class)
class CommunityCommentServiceImplTest {

    @InjectMocks
    private CommunityCommentServiceImpl communityCommentService;

    @Mock
    private CommunityRepository communityRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommunityCommentRepository communityCommentRepository;

    @Test
    @DisplayName("저장 - 최상위 댓글 - 작성 성공")
    void save() {
        // given
        CommunityCommentSaveRequestDto communityCommentSaveRequestDto = setUpSaveRequestDto();
        User user = setUpUser();
        Community community = setUpCommunity(user);
        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            CommunityComment communityComment = setUpParentComment(user, community);
            when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(user));
            when(communityRepository.findById(any())).thenReturn(Optional.ofNullable(community));
            when(communityCommentRepository.save(any())).thenReturn(communityComment);
            // when
            CommunityCommentSaveResponseDto save = communityCommentService.save(communityCommentSaveRequestDto);

            // then

            assertThat(save.getId()).isEqualTo(communityComment.getId());
            assertThat(save.getCommunityId()).isEqualTo(community.getId());
            assertThat(save.getContent()).isEqualTo(communityComment.getContent());

        }
    }
    @Test
    @DisplayName("저장 - 최상위 댓글 - 작성 실패 - user 존재 x")
    void saveFailWhenUserNotFound() {
        // given
        CommunityCommentSaveRequestDto communityCommentSaveRequestDto = setUpWrongSaveRequestDto();

        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(userRepository.findByUserId(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

            // when
            CustomException customException = assertThrows(CustomException.class, () -> communityCommentService.save(communityCommentSaveRequestDto));

            // then
            assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND);
        }


    }

    @Test
    @DisplayName("저장 - 최상위 댓글 - 작성 실패 - community 존재 x")
    void saveFailWhenCommunityNotFound() {
        // given
        CommunityCommentSaveRequestDto communityCommentSaveRequestDto = setUpWrongSaveRequestDto();
        User user = setUpUser();

        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(user));
            when(communityRepository.findById(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));

            // when
            CustomException customException = assertThrows(CustomException.class, () -> communityCommentService.save(communityCommentSaveRequestDto));

            // then
            assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.COMMUNITY_NOT_FOUND);
        }


    }

    @Test
    @DisplayName("저장 - 자식 댓글 - 작성 성공")
    void saveChild() {
        // given
        Long parentId = 1L;
        CommunityCommentSaveRequestDto communityCommentSaveRequestDto = setUpSaveRequestDto(parentId);
        User user = setUpUser();
        Community community = setUpCommunity(user);
        CommunityComment communityComment = setUpParentComment(user, community);
        CommunityComment childComment = setUpChildComment(user, community, communityComment);
        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(user));
            when(communityRepository.findById(any())).thenReturn(Optional.ofNullable(community));
            when(communityCommentRepository.findById(any())).thenReturn(Optional.ofNullable(communityComment));
            when(communityCommentRepository.save(any())).thenReturn(childComment);
            // when
            CommunityCommentSaveResponseDto save = communityCommentService.save(communityCommentSaveRequestDto);

            // then
            assertThat(save.getContent()).isEqualTo(childComment.getContent());
            assertThat(save.getCommunityId()).isEqualTo(community.getId());
            assertThat(save.getId()).isEqualTo(childComment.getId());


        }
    }
    @Test
    @DisplayName("저장 - 자식 댓글 - 작성 실패 - 부모 댓글 존재 x")
    void saveChildFailWhenParentCommentNotFound() {
        // given
        Long parentId = 1L;
        CommunityCommentSaveRequestDto communityCommentSaveRequestDto = setUpSaveRequestDto(parentId);
        User user = setUpUser();
        Community community = setUpCommunity(user);

        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(user));
            when(communityRepository.findById(any())).thenReturn(Optional.ofNullable(community));
            when(communityCommentRepository.findById(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));
            // when
            CustomException customException = assertThrows(CustomException.class, () -> communityCommentService.save(communityCommentSaveRequestDto));

            // then
            assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("저장 - 자식 댓글 - 작성 실패 - 댓글과 커뮤니티 id 불일치")
    void saveChildFailWhenCommunityAndCommentIdMismatch() {
        // given
        Long parentId = 1L;
        CommunityCommentSaveRequestDto communityCommentSaveRequestDto = setUpSaveRequestDto(parentId);
        User user = setUpUser();

        Community community = setUpCommunity(user);
        Community wrongCommunity = setUpWrongCommunity(user);

        CommunityComment communityComment = setUpParentComment(user, wrongCommunity);

        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(userRepository.findByUserId(any())).thenReturn(Optional.ofNullable(user));
            when(communityRepository.findById(any())).thenReturn(Optional.ofNullable(community));
            when(communityCommentRepository.findById(any())).thenReturn(Optional.ofNullable(communityComment));
            // when
            CustomException customException = assertThrows(CustomException.class, () -> communityCommentService.save(communityCommentSaveRequestDto));

            // then
            assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INVALID_COMMENT);
        }
    }

    @Test
    @DisplayName("삭제 성공")
    void deleteSuccess() {
        // given
        Long id = 1L;
        User user = setUpUser();
        Community community = setUpCommunity(user);
        CommunityComment communityComment = setUpParentComment(user, community);
        CommunityComment deletedComment = setUpDeletedComment(user, community);
        when(communityCommentRepository.findById(any())).thenReturn(Optional.ofNullable(communityComment));
        when(communityCommentRepository.save(any())).thenReturn(deletedComment);
        // when
        CommunityCommentDeleteResponseDto delete = communityCommentService.delete(id);

        // then
        assertThat(delete.getId()).isEqualTo(deletedComment.getId());
    }

    @Test
    @DisplayName("삭제 실패")
    void deleteFail() {
        // given
        Long id = 1L;

        when(communityCommentRepository.findById(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));
        // when
        CustomException customException = assertThrows(CustomException.class, () -> communityCommentService.delete(id));

        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("업데이트 성공")
    void updateSuccess() {
        // given
        Long id = 1L;
        CommunityCommentUpdateRequestDto communityCommentUpdateRequestDto = setUpUpdateRequest();
        User user = setUpUser();
        Community community = setUpCommunity(user);
        CommunityComment communityComment = setUpParentComment(user, community);
        CommunityComment updatedComment = setUpUpdatedComment(user, community);
        when(communityCommentRepository.findById(any())).thenReturn(Optional.ofNullable(communityComment));
        when(communityCommentRepository.save(any())).thenReturn(updatedComment);
        // when
        CommunityCommentUpdateResponseDto update = communityCommentService.update(communityCommentUpdateRequestDto);

        // then
        assertThat(update.getId()).isEqualTo(updatedComment.getId());
        assertThat(update.getContent()).isEqualTo(updatedComment.getContent());
    }

    @Test
    @DisplayName("업데이트 실패")
    void updateFail() {
        // given
        Long id = 1L;
        when(communityCommentRepository.findById(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));
        // when
        CustomException customException = assertThrows(CustomException.class, () -> communityCommentService.delete(id));

        // then
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("댓글 조회")
    void findCommentByCommunityId() {
        // given
        Long communityId = 1L;
        User user = setUpUser();
        Community community = setUpCommunity(user);
        List<CommunityComment> communityComments = setUpCommunityCommentFindResult(user, community);
        try (MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(communityCommentRepository.findByCommunityId(any())).thenReturn(communityComments);

            // when
            List<CommunityCommentFindResponseDto> result = communityCommentService.findCommentByCommunityId(communityId);

            // then
            assertThat(result).hasSize(10);
        }

    }

    private List<CommunityComment> setUpCommunityCommentFindResult(User user, Community community){
        List<CommunityComment> result = new ArrayList<>();

        for(int i =1; i<11; i++){
            CommunityComment communityComment = CommunityComment.builder()
                    .id((long) i)
                    .modified(false)
                    .deleted(false)
                    .content("test")
                    .createdAt(LocalDateTime.now())
                    .user(user)
                    .community(community)
                    .build();

            result.add(communityComment);
        }
        return result;
    }

    private CommunityCommentSaveRequestDto setUpSaveRequestDto(){
        return CommunityCommentSaveRequestDto.builder()
                .communityId(1L)
                .content("content")
                .build();
    }
    private CommunityCommentSaveRequestDto setUpSaveRequestDto(Long parentId){
        return CommunityCommentSaveRequestDto.builder()
                .communityId(1L)
                .content("content")
                .parentId(parentId)
                .build();
    }
    private CommunityCommentSaveRequestDto setUpWrongSaveRequestDto(){
        return CommunityCommentSaveRequestDto.builder()
                .communityId(2L)
                .content("content")
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

    private User setUpWrongUser(){
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
    private CommunityCommentUpdateRequestDto setUpUpdateRequest(){
        return CommunityCommentUpdateRequestDto.builder()
                .id(1L)
                .content("modified")
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
    private Community setUpWrongCommunity(User user){
        return Community.builder()
                .id(2L)
                .title("test title")
                .content("test content")
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }

    private CommunityComment setUpUpdatedComment(User user, Community community){
        return CommunityComment.builder()
                .id(1L)
                .modified(true)
                .deleted(false)
                .content("test")
                .createdAt(LocalDateTime.now())
                .user(user)
                .community(community)
                .build();
    }
    private CommunityComment setUpParentComment(User user, Community community){
        return CommunityComment.builder()
                .id(1L)
                .modified(false)
                .deleted(false)
                .content("test")
                .createdAt(LocalDateTime.now())
                .user(user)
                .community(community)
                .build();
    }
    private CommunityComment setUpDeletedComment(User user, Community community){
        return CommunityComment.builder()
                .id(1L)
                .modified(false)
                .deleted(true)
                .content("test")
                .createdAt(LocalDateTime.now())
                .user(user)
                .community(community)
                .build();
    }
    private CommunityComment setUpChildComment(User user, Community comment, CommunityComment communityComment) {
        return CommunityComment.builder()
                .modified(false)
                .deleted(false)
                .content("test")
                .createdAt(LocalDateTime.now())
                .user(user)
                .community(comment)
                .parentComment(communityComment)
                .build();
    }
}