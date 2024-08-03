package com.alldriver.alldriver.community.controller;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.community.dto.response.CommunityBookmarkDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityBookmarkSaveResponseDto;
import com.alldriver.alldriver.community.service.CommunityBookmarkService;
import com.alldriver.alldriver.user.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityBookmarkController.class)
@WithMockUser(username = "testUser")
class CommunityBookmarkControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private CommunityBookmarkService communityBookmarkService;

    @Test
    @DisplayName("저장 성공")
    void save() throws Exception {
        // given
        CommunityBookmarkSaveResponseDto responseDto = setUpSaveResponse();
        when(communityBookmarkService.save(any())).thenReturn(responseDto);

        // when, then
        mockMvc.perform(post("/user/community/bookmark/save/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.communityId").value(1L))
                .andExpect(jsonPath("$.bookmarkId").value(1L))
                .andExpect(jsonPath("$.userId").value("testUser"));
    }

    @Test
    @DisplayName("저장 실패 - 커뮤니티 미존재")
    void saveFailWhenCommunityNotFound() throws Exception {
        // given
        when(communityBookmarkService.save(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));

        // when, then
        mockMvc.perform(post("/user/community/bookmark/save/1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.COMMUNITY_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.COMMUNITY_NOT_FOUND.getMessage()));
    }
    @Test
    @DisplayName("저장 실패 - 유저 미존재")
    void saveFailWhenUserNotFound() throws Exception {
        // given
        when(communityBookmarkService.save(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // when, then
        mockMvc.perform(post("/user/community/bookmark/save/1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));
    }
    @Test
    @DisplayName("저장 실패 - 중복 즐겨찾기")
    void saveFailWhenDuplicatedBookmark() throws Exception {
        // given
        when(communityBookmarkService.save(any())).thenThrow(new CustomException(ErrorCode.DUPLICATED_BOOKMARK));

        // when, then
        mockMvc.perform(post("/user/community/bookmark/save/1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATED_BOOKMARK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATED_BOOKMARK.getMessage()));
    }

    @Test
    @DisplayName("삭제 성공")
    void delete() throws Exception {
        // given
        CommunityBookmarkDeleteResponseDto responseDto = setUpDeleteResponse();
        when(communityBookmarkService.delete(any())).thenReturn(responseDto);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/community/bookmark/delete/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

    }


    @Test
    @DisplayName("삭제 실패 - 즐겨찾기 미존재")
    void deleteFailWhenBookmarkNotFound() throws Exception {
        // given
        when(communityBookmarkService.delete(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_BOOKMARK_NOT_FOUND));

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/community/bookmark/delete/1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.COMMUNITY_BOOKMARK_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.COMMUNITY_BOOKMARK_NOT_FOUND.getMessage()));
    }

    private CommunityBookmarkSaveResponseDto setUpSaveResponse(){
        return CommunityBookmarkSaveResponseDto.builder()
                .communityId(1L)
                .bookmarkId(1L)
                .userId("testUser")
                .build();
    }

    private CommunityBookmarkDeleteResponseDto setUpDeleteResponse(){
        return CommunityBookmarkDeleteResponseDto.builder()
                .id(1L)
                .build();
    }
}