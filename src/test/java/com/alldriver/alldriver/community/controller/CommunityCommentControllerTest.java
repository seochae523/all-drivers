package com.alldriver.alldriver.community.controller;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.community.dto.request.CommunityCommentSaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityCommentUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentFindResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentSaveResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentUpdateResponseDto;
import com.alldriver.alldriver.community.service.CommunityBookmarkService;
import com.alldriver.alldriver.community.service.CommunityCommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommunityCommentController.class)
@WithMockUser(username = "testUser")
class CommunityCommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommunityCommentService communityCommentService;

    @Test
    @DisplayName("최상위 댓글 저장 성공")
    void saveParentComment() throws Exception {
        // given
        CommunityCommentSaveRequestDto requestDto = setUpParentCommentSaveRequest();
        CommunityCommentSaveResponseDto responseDto = setUpSaveResponse();
        when(communityCommentService.save(any())).thenReturn(responseDto);

        // when, then
        mockMvc.perform(post("/user/community/comment/save").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.communityId").value(1L))
                .andExpect(jsonPath("$.content").value("test"));

    }

    @Test
    @DisplayName("자식 댓글 저장 성공")
    void saveChildComment() throws Exception {
        // given
        CommunityCommentSaveRequestDto requestDto = setUpChildCommentSaveRequest();
        CommunityCommentSaveResponseDto responseDto = setUpSaveResponse();
        when(communityCommentService.save(any())).thenReturn(responseDto);

        // when, then
        mockMvc.perform(post("/user/community/comment/save").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.communityId").value(1L))
                .andExpect(jsonPath("$.content").value("test"));

    }

    @Test
    @DisplayName("댓글 저장 실패 - 커뮤니티 미존재")
    void saveFailWhenCommunityNotFound() throws Exception {
        // given
        CommunityCommentSaveRequestDto requestDto = setUpParentCommentSaveRequest();
        when(communityCommentService.save(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));

        // when, then
        mockMvc.perform(post("/user/community/comment/save").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.COMMUNITY_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.COMMUNITY_NOT_FOUND.getMessage()));

    }
    @Test
    @DisplayName("댓글 저장 실패 - 유저 미존재")
    void saveFailWhenUserNotFound() throws Exception {
        // given
        CommunityCommentSaveRequestDto requestDto = setUpParentCommentSaveRequest();
        when(communityCommentService.save(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // when, then
        mockMvc.perform(post("/user/community/comment/save").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));

    }
    @Test
    @DisplayName("댓글 저장 실패 - 부모 댓글 미존재")
    void saveFailWhenParentCommentNotFound() throws Exception {
        // given
        CommunityCommentSaveRequestDto requestDto = setUpChildCommentSaveRequest();
        when(communityCommentService.save(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));

        // when, then
        mockMvc.perform(post("/user/community/comment/save").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND.getMessage()));

    }
    @Test
    @DisplayName("댓글 저장 실패 - 댓글과 게시글 미일치")
    void saveFailWhenCommunityAndCommentMissMatch() throws Exception {
        // given
        CommunityCommentSaveRequestDto requestDto = setUpChildCommentSaveRequest();
        when(communityCommentService.save(any())).thenThrow(new CustomException(ErrorCode.INVALID_COMMENT));

        // when, then
        mockMvc.perform(post("/user/community/comment/save").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_COMMENT.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_COMMENT.getMessage()));

    }
    @Test
    @DisplayName("댓글 업데이트 성공")
    void update() throws Exception {
        // given
        CommunityCommentUpdateRequestDto requestDto = setUpUpdateRequest();
        CommunityCommentUpdateResponseDto responseDto = setUpUpdateResponse();
        when(communityCommentService.update(any())).thenReturn(responseDto);

        // when, then
        mockMvc.perform(put("/user/community/comment/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("updated"));
    }
    @Test
    @DisplayName("댓글 업데이트 실패 - 댓글 미존재")
    void updateFailWhenCommunityNotFound() throws Exception {
        // given
        CommunityCommentUpdateRequestDto requestDto = setUpUpdateRequest();

        when(communityCommentService.update(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));

        // when, then
        mockMvc.perform(put("/user/community/comment/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("삭제 성공")
    void delete() throws Exception {
        // given
        CommunityCommentDeleteResponseDto responseDto = setUpDeleteResponse();
        when(communityCommentService.delete(any())).thenReturn(responseDto);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/community/comment/delete/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    @DisplayName("삭제 실패 - 댓글 미존재")
    void deleteFailWhenCommentNotFound() throws Exception {
        // given

        when(communityCommentService.delete(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/community/comment/delete/1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code" ).value(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND.getMessage()));

    }
    @Test
    @DisplayName("커뮤니티에 따른 댓글 조회 성공")
    void findCommentsByCommunityId() throws Exception {
        // given
        List<CommunityCommentFindResponseDto> responseDtos = setUpFindList();
        when(communityCommentService.findCommentByCommunityId(any())).thenReturn(responseDtos);

        // when, then
        mockMvc.perform(get("/user/community/comment/comments/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDtos)));
    }

    private CommunityCommentSaveRequestDto setUpParentCommentSaveRequest(){
        return CommunityCommentSaveRequestDto.builder()
                .communityId(1L)
                .content("test")
                .build();
    }
    private CommunityCommentSaveRequestDto setUpChildCommentSaveRequest(){
        return CommunityCommentSaveRequestDto.builder()
                .communityId(1L)
                .parentId(1L)
                .content("test")
                .build();
    }

    private CommunityCommentSaveResponseDto setUpSaveResponse(){
        return CommunityCommentSaveResponseDto.builder()
                .id(1L)
                .communityId(1L)
                .content("test")
                .build();
    }

    private CommunityCommentUpdateRequestDto setUpUpdateRequest(){
        return CommunityCommentUpdateRequestDto.builder()
                .id(1L)
                .content("updated")
                .build();
    }
    private CommunityCommentUpdateResponseDto setUpUpdateResponse(){
        return CommunityCommentUpdateResponseDto.builder()
                .id(1L)
                .content("updated")
                .build();
    }

    private CommunityCommentDeleteResponseDto setUpDeleteResponse(){
        return CommunityCommentDeleteResponseDto.builder()
                .id(1L)
                .build();
    }

    private List<CommunityCommentFindResponseDto> setUpFindList(){
        List<CommunityCommentFindResponseDto> result = new ArrayList<>();
        for(int i =1; i<11; i++) {
            CommunityCommentFindResponseDto build = CommunityCommentFindResponseDto.builder()
                    .id((long) i)
                    .modified(false)
                    .createdAt(LocalDateTime.now())
                    .content("test")
                    .build();

            result.add(build);
        }
        return result;
    }
}