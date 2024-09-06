package com.alldriver.alldriver.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class BoardBookmarkSaveResponseDto {
    private Long boardId;
    private String userId;
    private Long bookmarkId;
}
