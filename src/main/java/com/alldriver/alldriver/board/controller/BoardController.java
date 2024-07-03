package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.dto.response.*;
import com.alldriver.alldriver.board.service.BoardCategoryRetrieveService;
import com.alldriver.alldriver.board.service.BoardRetrieveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alldriver.alldriver.board.dto.request.BoardSaveRequestDto;
import com.alldriver.alldriver.board.service.BoardService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "board")
public class BoardController {
    private final BoardService boardService;

    @Operation(description = "게시글을 form-data 타입을 통해 저장 (json x)")
    @PostMapping("/board/save")
    @Parameters({
            @Parameter(name="images", description = "이미지 첨부"),
            @Parameter(name="request", description = "게시글에 필요한 리퀘스트", required = true)
    })
    public ResponseEntity<BoardSaveResponseDto> save(@RequestPart(value = "images", required = false) List<MultipartFile> multipartFile,
                                                     @RequestPart(value = "request", required = false) BoardSaveRequestDto boardSaveRequestDto) throws IOException {
        return ResponseEntity.ok(boardService.save(multipartFile, boardSaveRequestDto));
    }

}
