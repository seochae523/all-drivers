package com.alldriver.alldriver.board.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardUpdateResponseDto {

    private String content;
    private String title;
    private Integer price;
}
