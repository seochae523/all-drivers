package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.dto.request.BoardUpdateRequestDto;
import com.alldriver.alldriver.board.dto.response.*;
import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alldriver.alldriver.board.dto.request.BoardSaveRequestDto;
import com.alldriver.alldriver.board.service.BoardService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/board")
@Tag(name = "게시글 관련 api")
@Validated
public class BoardController {
    private final BoardService boardService;

    @Operation(description = "게시글을 form-data 타입을 통해 저장 (json x)")
    @PostMapping("/save")
    @Parameters({
            @Parameter(name="images", description = "이미지 첨부"),
            @Parameter(name="request", description = "게시글에 필요한 리퀘스트", required = true)
    })
    public ResponseEntity<BoardSaveResponseDto> save(@RequestPart(value = "images", required = false) List<MultipartFile> multipartFile,
                                                     @RequestPart(value = "request")
                                                     @Valid BoardSaveRequestDto boardSaveRequestDto) throws IOException {
        return ResponseEntity.ok(boardService.save(multipartFile, boardSaveRequestDto));
    }

    @Operation(description = "게시글 업데이트")
    @PutMapping("/update")
    @Parameters({
            @Parameter(name="images", description = "추가 할 이미지"),
            @Parameter(name="request", description = "게시글에 필요한 리퀘스트", required = true)
    })
    public ResponseEntity<BoardUpdateResponseDto> update(@RequestPart(value = "images", required = false) List<MultipartFile> multipartFile,
                                                         @RequestPart(value = "request")
                                                         @Valid BoardUpdateRequestDto boardUpdateRequestDto) throws IOException {
        return ResponseEntity.ok(boardService.update(multipartFile, boardUpdateRequestDto));
    }
    @Operation(description = "게시글 삭제")
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<BoardDeleteResponseDto> delete(@PathVariable
                                                         @NotNull(message = ValidationError.Message.BOARD_ID_NOT_FOUND) Long boardId){
        return ResponseEntity.ok(boardService.delete(boardId));
    }
}
