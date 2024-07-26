package com.alldriver.alldriver.chat.dto.response;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomSaveResponseDto {
    private Long id;
    private String publisher;
    private String subscriber;
    private String boardTitle;
    private Date createdAt;
    private Boolean isCreated;
}
