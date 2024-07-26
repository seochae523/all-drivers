package com.alldriver.alldriver.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChatSaveResponseDto {
    private Long roomId;
    private String message;
    private String sender;
    private Date createdAt;
}
