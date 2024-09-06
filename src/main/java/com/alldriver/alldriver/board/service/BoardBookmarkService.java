package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.dto.response.BoardBookmarkDeleteResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardBookmarkSaveResponseDto;

public interface BoardBookmarkService {
    BoardBookmarkSaveResponseDto saveLike(Long boardId);
    BoardBookmarkDeleteResponseDto deleteLike(Long boardId);
}
