package com.alldriver.alldriver.board.service.impl;

import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.domain.BoardBookmark;
import com.alldriver.alldriver.board.dto.response.BoardBookmarkDeleteResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardBookmarkSaveResponseDto;
import com.alldriver.alldriver.board.repository.BoardRepository;
import com.alldriver.alldriver.board.repository.BoardBookmarkRepository;
import com.alldriver.alldriver.board.service.BoardBookmarkService;
import com.alldriver.alldriver.common.enums.ErrorCode;
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
    private final BoardBookmarkRepository boardBookmarkRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    @Override
    public BoardBookmarkSaveResponseDto saveLike(Long boardId) {
        String userId = JwtUtils.getUserId();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        boardBookmarkRepository.findByBoardIdAndUserId(boardId, userId)
                .ifPresent(x -> {
                    throw new CustomException(ErrorCode.DUPLICATED_BOOKMARK);
                });

        BoardBookmark boardBookmark = BoardBookmark.builder()
                .board(board)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        BoardBookmark save = boardBookmarkRepository.save(boardBookmark);

        return BoardBookmarkSaveResponseDto.builder()
                .boardId(board.getId())
                .userId(user.getUserId())
                .bookmarkId(save.getId())
                .build();
    }

    @Override
    public BoardBookmarkDeleteResponseDto deleteLike(Long boardId) {
        String userId = JwtUtils.getUserId();

        BoardBookmark boardBookmark = boardBookmarkRepository.findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        boardBookmarkRepository.delete(boardBookmark);

        return BoardBookmarkDeleteResponseDto.builder()
                .bookmarkId(boardBookmark.getId())
                .build();
    }
}
