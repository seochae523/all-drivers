package com.alldriver.alldriver.chat.dto.response;


import com.alldriver.alldriver.chat.controller.ChatRoomController;
import com.alldriver.alldriver.chat.domain.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomFindResponseDto {
    private Long roomId;
    private String creator;
    private LocalDateTime createdAt;
    private String title;
    public ChatRoomFindResponseDto(ChatRoom chatRoom, String creator){
        this.roomId = chatRoom.getId();
        this.creator = creator;
        this.createdAt = chatRoom.getCreatedAt();
        this.title = chatRoom.getTitle();
    }
}
