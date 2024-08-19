package com.alldriver.alldriver.chat.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ChatRoomParticipateResponseDto {
    private Long roomId;
    private String userId;
}
