package com.alldriver.alldriver.community.controller;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.community.dto.response.CommunityFindResponseDto;
import com.alldriver.alldriver.community.service.impl.CommunityRetrieveServiceImpl;
import com.alldriver.alldriver.community.service.impl.CommunityServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommunityRetrieveController.class)
@WithMockUser("test")
class CommunityRetrieveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommunityRetrieveServiceImpl communityRetrieveService;
    @Test
    @DisplayName("전체 조회")
    void findAll() throws Exception {
        // given
        List<CommunityFindResponseDto> response = setUpFindResponse();
        when(communityRetrieveService.findAll(any())).thenReturn(response);

        // when, then
        mockMvc.perform(get("/user/community/all").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }


    @Test
    @DisplayName("유저로 조회")
    void findByUserId() throws Exception {
        // given
        List<CommunityFindResponseDto> response = setUpFindResponse();
        when(communityRetrieveService.findByUserId(any())).thenReturn(response);

        // when, then
        mockMvc.perform(get("/user/community/userId").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("지역으로 조회 - id 존재")
    void findBySubLocationId() throws Exception {
        // given
        List<CommunityFindResponseDto> response = setUpFindResponse();
        when(communityRetrieveService.findBySubLocationId(any(), any())).thenReturn(response);

        // when, then
        mockMvc.perform(get("/user/community/subLocation").with(csrf())
                        .param("subLocationId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }



    private List<CommunityFindResponseDto> setUpFindResponse(){
        List<CommunityFindResponseDto> result = new ArrayList<>();

        for(int i=1; i<11; i++){
            CommunityFindResponseDto build = CommunityFindResponseDto.builder()
                    .id((long) i)
                    .title("test")
                    .content("test")
                    .createdAt(LocalDateTime.now())
                    .userId("test")
                    .bookmarked(0)
                    .bookmarkCount(i)
                    .build();

            result.add(build);
        }
        return result;
    }
}