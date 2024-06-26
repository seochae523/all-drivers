package com.alldriver.alldriver.board.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardUpdateRequestDto {
    @Schema(description = "업데이트 할 id 식별용", example = "2")
    private Long id;
    @Schema(description = "업데이트 할 내용", example = "내용내뇽내용")
    private String content;
    @Schema(description = "업데이트 할 제목", example = "제목몾고족")
    private String title;

    @Schema(description = "업데이트 할 이미지", example = "파일임 json 아님")
    private List<ImageUpdateResponseDto> imageUpdateResponseDto;
}
