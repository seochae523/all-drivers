package com.alldriver.alldriver.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardSaveResponseDto {
    @Schema(description = "내용", example = "example")
    private String content;
    @Schema(description = "제목", example = "example")
    private String title;
    @Schema(description = "작성자", example = "example")
    private String studentId;
}
