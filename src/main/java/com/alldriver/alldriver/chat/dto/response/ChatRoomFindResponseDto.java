package com.alldriver.alldriver.chat.dto.response;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomFindResponseDto {
    private Long roomId;
    private String publisher;
    private String subscriber;
    private Date createdAt;
    private String boardTitle;
}
