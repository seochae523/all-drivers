package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.user.dto.response.ChangePasswordResponseDto;
import com.alldriver.alldriver.user.dto.response.DeleteResponseDto;
import com.alldriver.alldriver.user.dto.response.UserUpdateResponseDto;
import com.alldriver.alldriver.user.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @Test
    @WithMockUser("testUser")
    @DisplayName("닉네임 중복 확인")
    void 닉네임_중복_확인() throws Exception {
        // given
        when(userService.checkNickname("test")).thenReturn(true);
        when(userService.checkNickname("duplicatedUser")).thenThrow(new CustomException(ErrorCode.DUPLICATED_NICKNAME));

        // when, then
        mockMvc.perform(get("/check/nickname?nickname=test"))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.valueOf(true).toString()));

        mockMvc.perform(get("/check/nickname?nickname=duplicatedUser"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("AEU-005"));
    }

    @Test
    @WithMockUser("testUser")
    @DisplayName("삭제 확인")
    void 삭제_확인() throws Exception {
        // given
        when(userService.signOut("testUserId")).thenReturn("회원 탈퇴 완료.");

        // when, then
        mockMvc.perform(delete("/user/sign-out?userId=testUserId").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("회원 탈퇴 완료."));

    }
    @Test
    @WithMockUser("testUser")
    @DisplayName("업데이트")
    void 업데이트() throws Exception {
        // given
        Map<String, String> content = new HashMap<>();
        content.put("userId", "testUserId");
        content.put("nickname", "testUserNickname");
        UserUpdateResponseDto userUpdateResponseDto = UserUpdateResponseDto.builder()
                                                                                .userId("testUserId")
                                                                                .nickname("testUserNickname")
                                                                                .build();
        when(userService.update(any())).thenReturn(userUpdateResponseDto);

        // when, then
        mockMvc.perform(put("/user/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(content)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testUserId"))
                .andExpect(jsonPath("$.nickname").value("testUserNickname"));
    }
    @Test
    @WithMockUser("testUser")
    @DisplayName("비밀번호 변경")
    void 비밀번호_변경() throws Exception {
        // given
        Map<String, String> content = new HashMap<>();
        content.put("userId", "testUser");
        content.put("password", "testPassword");
        ChangePasswordResponseDto changePasswordResponseDto = ChangePasswordResponseDto.builder()
                .userId("testUser")
                .build();


        when(userService.changePassword(any())).thenReturn(changePasswordResponseDto);
        // when

        // then
        mockMvc.perform(put("/user/change-password").with(csrf())
                .content(objectMapper.writeValueAsString(content))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testUser"));
    }
    @Test
    @WithMockUser("testUser")
    @DisplayName("잊어버린 비밀번호 변경")
    void 잊어버린_비밀번호_변경() throws Exception {
        // given
        Map<String, String> content = new HashMap<>();
        content.put("userId", "testUser");
        content.put("password", "testPassword");
        ChangePasswordResponseDto changePasswordResponseDto = ChangePasswordResponseDto.builder()
                .userId("testUser")
                .build();


        when(userService.changePassword(any())).thenReturn(changePasswordResponseDto);
        // when

        // then

        mockMvc.perform(put("/change-forget-password").with(csrf())
                        .content(objectMapper.writeValueAsString(content))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("testUser"));
    }

}
