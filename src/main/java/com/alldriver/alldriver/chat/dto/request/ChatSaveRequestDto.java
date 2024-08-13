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

    @NotNull(message = "패팅 방 id가 존재하지 않습니다.")
    private Long roomId;

    @NotBlank(message = "채팅 메시지가 존재하지 않습니다.")
    private String message;

    @NotBlank(message = "메시지 발송자가 존재하지 않습니다.")
    private String sender;
}
