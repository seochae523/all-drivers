package com.alldriver.alldriver.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class BoardBookmarkSaveResponseDto {
    private Long boardId;
    private String userId;
    private Long bookmarkId;
}
