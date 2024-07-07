package com.alldriver.alldriver.board.service.impl;

import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.domain.Bookmark;
import com.alldriver.alldriver.board.repository.BoardRepository;
import com.alldriver.alldriver.board.repository.BookmarkRepository;
import com.alldriver.alldriver.board.service.BoardBookmarkService;
import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BoardBookmarkServiceImpl implements BoardBookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    @Override
    public String saveLike(Long boardId) {
        String userId = JwtUtils.getUserId();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));

        Bookmark bookmark = Bookmark.builder()
                .board(board)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        bookmarkRepository.save(bookmark);

        return "관심 목록 저장 완료.";
    }

    @Override
    public String deleteLike(Long boardId) {
        String userId = JwtUtils.getUserId();

        Bookmark bookmark = bookmarkRepository.findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));

        bookmarkRepository.delete(bookmark);

        return "관심 목록 삭제 완료.";
    }
}
