package com.alldriver.alldriver.chat.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomCreateResponseDto {
    private Long roomId;
    private String studentId;
    private Date createdAt;
}
