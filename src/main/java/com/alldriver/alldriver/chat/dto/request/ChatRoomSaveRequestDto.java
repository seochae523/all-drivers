package com.alldriver.alldriver.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomSaveRequestDto {
    @NotBlank(message = "채팅방 제목이 존재하지 않습니다.")
    @Schema(description = "방 제목", example = "ex")
    private String title;

}
