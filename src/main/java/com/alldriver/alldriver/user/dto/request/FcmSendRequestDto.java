package com.alldriver.alldriver.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FcmSendRequestDto {
    @Schema(description = "알림 제목", example = "something title")
    private String title;

    @Schema(description = "알림 내용", example = "something content")
    private String body;
}
