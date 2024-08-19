package com.alldriver.alldriver.chat.dto.response;

import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomFindDetailResponseDto {
    private Long roomId;
    private List<String> participants;
}
