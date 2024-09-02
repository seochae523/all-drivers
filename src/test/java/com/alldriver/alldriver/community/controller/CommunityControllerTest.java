package com.alldriver.alldriver.community.controller;

import com.alldriver.alldriver.board.controller.BoardRetrieveController;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.community.dto.request.CommunitySaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunitySaveResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityUpdateResponseDto;
import com.alldriver.alldriver.community.service.CommunityCommentService;
import com.alldriver.alldriver.community.service.impl.CommunityServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityController.class)
@WithMockUser("test")
class CommunityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommunityServiceImpl communityService;


    @Test
    @DisplayName("저장 성공")
    void save() throws Exception {
        // given
        CommunitySaveRequestDto requestDto = setUpSaveRequest();
        CommunitySaveResponseDto responseDto = setUpSaveResponse();
        when(communityService.save(any())).thenReturn(responseDto);

        // when, then
        mockMvc.perform(post("/user/community/save").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("test"))
                .andExpect(jsonPath("$.content").value("test"));

    }

    @Test
    @DisplayName("저장 실패 - 유저 미존재")
    void saveFailWhenUserNotFound() throws Exception{
        // given
        CommunitySaveRequestDto requestDto = setUpSaveRequest();
        when(communityService.save(any())).thenThrow(new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // when, then
        mockMvc.perform(post("/user/community/save").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));
    }
    @Test
    @DisplayName("저장 실패 - 지역 미존재")
    void saveFailWhenSubLocationNotFound() throws Exception{
        // given
        CommunitySaveRequestDto requestDto = setUpSaveRequest();
        when(communityService.save(any())).thenThrow(new CustomException(ErrorCode.SUB_LOCATION_NOT_FOUND));

        // when, then
        mockMvc.perform(post("/user/community/save").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.SUB_LOCATION_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.SUB_LOCATION_NOT_FOUND.getMessage()));
    }
    @Test
    @DisplayName("업데이트 성공")
    void update() throws Exception {
        // given
        CommunityUpdateRequestDto requestDto = setUpUpdateRequest();
        CommunityUpdateResponseDto responseDto = setUpUpdateResponse();
        when(communityService.update(any())).thenReturn(responseDto);

        // when, then
        mockMvc.perform(put("/user/community/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("modified"))
                .andExpect(jsonPath("$.content").value("modified"));

    }

    @Test
    @DisplayName("업데이트 실패 - 커뮤니티 미존재")
    void updateFailWhenCommunityNotFound() throws Exception{
        // given
        CommunityUpdateRequestDto requestDto = setUpUpdateRequest();
        when(communityService.update(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));

        // when, then
        mockMvc.perform(put("/user/community/update").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.COMMUNITY_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.COMMUNITY_NOT_FOUND.getMessage()));
    }


    @Test
    @DisplayName("삭제 성공")
    void delete() throws Exception {
        // given
        CommunityDeleteResponseDto responseDto = setUpDeleteResponse();
        when(communityService.delete(any())).thenReturn(responseDto);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/community/delete/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    @DisplayName("삭제 실패 - 커뮤니티 미존재")
    void deleteFailWhenCommunityNotFound() throws Exception{
        // given
        when(communityService.delete(any())).thenThrow(new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/community/delete/1").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.COMMUNITY_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.COMMUNITY_NOT_FOUND.getMessage()));
    }
    private CommunitySaveRequestDto setUpSaveRequest(){
        return CommunitySaveRequestDto.builder()
                .title("test")
                .content("test")

                .build();
    }

    private CommunitySaveResponseDto setUpSaveResponse(){
        return CommunitySaveResponseDto.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .content("test")
                .title("test")
                .build();
    }

    private CommunityUpdateRequestDto setUpUpdateRequest(){
        return CommunityUpdateRequestDto.builder()
                .id(1L)
                .content("modified")
                .title("modified")
                .build();
    }

    private CommunityUpdateResponseDto setUpUpdateResponse(){
        return CommunityUpdateResponseDto.builder()
                .id(1L)
                .content("modified")
                .title("modified")
                .build();
    }
    private CommunityDeleteResponseDto setUpDeleteResponse(){
        return CommunityDeleteResponseDto.builder()
                .id(1L)
                .build();
    }
}