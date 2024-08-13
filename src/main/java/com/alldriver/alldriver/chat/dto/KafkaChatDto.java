package com.alldriver.alldriver.chat.dto;

import lombok.*;
import com.alldriver.alldriver.chat.dto.request.ChatSaveRequestDto;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaChatDto {
    private Long roomId;
    private String message;
    private String sender;
    private Date createdAt;

    public KafkaChatDto(ChatSaveRequestDto chatSaveRequestDto, Date createdAt, String sender){
        this.roomId = chatSaveRequestDto.getRoomId();
        this.message = chatSaveRequestDto.getMessage();
        this.sender = sender;
        this.createdAt = createdAt;
    }
}
