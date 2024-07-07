package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.service.BoardBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "board bookmark")
public class BoardBookmarkController {
    private final BoardBookmarkService boardBookmarkService;

    @Operation(description = "게시글 관심 목록 저장")
    @PostMapping("/board/bookmark/save")
    public ResponseEntity<String> save(@RequestParam(value = "boardId") Long boardId)  {
        return ResponseEntity.ok(boardBookmarkService.saveLike(boardId));
    }

    @Operation(description = "게시글 관심 목록 삭제")
    @DeleteMapping("/board/bookmark/delete")
    public ResponseEntity<String> delete(@RequestParam(value = "boardId") Long boardId) {
        return ResponseEntity.ok(boardBookmarkService.deleteLike(boardId));
    }
}
