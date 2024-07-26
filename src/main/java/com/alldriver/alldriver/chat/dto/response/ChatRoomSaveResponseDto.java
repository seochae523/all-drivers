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
    private String title;
    private String creator;
    private Date createdAt;

}
