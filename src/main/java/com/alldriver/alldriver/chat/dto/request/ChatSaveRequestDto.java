package com.alldriver.alldriver.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatSaveRequestDto {
    @NotNull(message = "Chat Room Id Not Found.")
    @PositiveOrZero(message = "Chat Room Id Must Be Positive Or Zero Value.")
    @Schema(description = "채팅 방 번호", example = "1")
    private Long roomId;

    @NotBlank(message = "Chat Message Not Found.")
    @Schema(description = "채팅 메시지", example = "hello world!")
    private String message;

}
