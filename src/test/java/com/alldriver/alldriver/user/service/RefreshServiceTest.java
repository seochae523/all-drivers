package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.JwtException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.dto.request.RefreshRequestDto;
import com.alldriver.alldriver.user.dto.response.AuthToken;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.impl.RefreshServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshServiceTest {
    @InjectMocks
    RefreshServiceImpl refreshService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("리프레쉬 성공")
    void refresh() {
        //given
        RefreshRequestDto request = RefreshRequestDto.builder().refreshToken("test").build();
        User user = User.builder()
                .name("test")
                .userId("test")
                .password("test")
                .refreshToken("123")
                .role("test")
                .build();
        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(JwtUtils.getUserIdFromRefreshToken(any())).thenReturn("test");
            when(JwtUtils.generateToken(any(), any())).thenReturn(new AuthToken("test", "test", "test"));
            when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));
            //when
            AuthToken refresh = refreshService.refresh(request);
            //then
            assertThat(refresh.getAccessToken()).isNotNull();
        }

    }
    @Test
    @DisplayName("리프레쉬 실패")
    void refreshFail(){
        //given
        RefreshRequestDto request = RefreshRequestDto.builder().refreshToken("test").build();

        try(MockedStatic<JwtUtils> jwtUtils = mockStatic(JwtUtils.class)) {
            when(JwtUtils.validateToken(any())).thenThrow(new JwtException(ErrorCode.INVALID_REFRESH_TOKEN));

            //when
            JwtException jwtException = assertThrows(JwtException.class, () -> refreshService.refresh(request));
            //then
            assertThat(jwtException.getErrorCode()).isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}