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
    @NotBlank(message = "Chat Room Publisher Not Found.")
    @Schema(description = "방 생성자", example = "ex")
    private String publisher;

    @NotBlank(message = "Chat Room Subscriber Not Found.")
    @Schema(description = "방 참여자", example = "ex")
    private String subscriber;

    @NotNull(message = "Board Id Not Found.")
    @PositiveOrZero(message = "Board Id Must Be Positive Or Zero Value.")
    @Schema(description = "게시판 id", example = "1")
    private Long boardId;
}
