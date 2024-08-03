package com.alldriver.alldriver.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class BoardUpdateResponseDto {
    private Long id;
    private String title;
    private String content;
}
