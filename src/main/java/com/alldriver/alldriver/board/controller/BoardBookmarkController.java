package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.dto.response.BoardBookmarkDeleteResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardBookmarkSaveResponseDto;
import com.alldriver.alldriver.board.service.BoardBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user/board/bookmark")
@Tag(name = "게시글 즐겨찾기 관련 api")
public class BoardBookmarkController {
    private final BoardBookmarkService boardBookmarkService;

    @Operation(description = "게시글 관심 목록 저장")
    @PostMapping("/save/{boardId}")
    public ResponseEntity<BoardBookmarkSaveResponseDto> save(@PathVariable Long boardId)  {
        return ResponseEntity.ok(boardBookmarkService.saveLike(boardId));
    }

    @Operation(description = "게시글 관심 목록 삭제")
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<BoardBookmarkDeleteResponseDto> delete(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardBookmarkService.deleteLike(boardId));
    }
}
