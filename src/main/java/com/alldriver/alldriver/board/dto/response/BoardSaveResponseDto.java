package com.alldriver.alldriver.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardSaveResponseDto {
    private Long id;

    private String content;

    private String title;

    private String userId;
}
