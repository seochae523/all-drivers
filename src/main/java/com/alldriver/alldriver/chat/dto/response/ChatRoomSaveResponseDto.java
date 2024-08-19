package com.alldriver.alldriver.chat.dto.response;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomSaveResponseDto {
    private Long roomId;
    private String title;
    private String creator;
    private LocalDateTime createdAt;

}
