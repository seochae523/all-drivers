package com.alldriver.alldriver.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.alldriver.alldriver.chat.domain.Chat;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChatFindResponseDto {
    private Long roomId;
    private String message;
    private String sender;
    private Date createdAt;

    public ChatFindResponseDto(Chat chat) {
        this.roomId = chat.getId();
        this.message = chat.getMessage();
        this.sender = chat.getSender();
        this.createdAt = chat.getCreatedAt();
    }

}
