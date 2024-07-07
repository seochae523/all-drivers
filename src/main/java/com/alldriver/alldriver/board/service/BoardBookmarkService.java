package com.alldriver.alldriver.board.service;

public interface BoardBookmarkService {
    String saveLike(Long boardId);
    String deleteLike(Long boardId);
}
