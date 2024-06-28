package com.alldriver.alldriver.board.controller;

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
import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardSaveResponseDto;
import com.alldriver.alldriver.board.service.BoardService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/board")
@Tag(name = "board")
public class BoardController {
    private final BoardService boardService;

    @Operation(summary = "게시글 저장",
            description = "게시글을 form-data 타입을 통해 저장 (json x)")
    @PostMapping("/save")
    @Parameters({
            @Parameter(name="file", description = "파일 첨부"),
            @Parameter(name="request", description = "게시글에 필요한 리퀘스트", required = true)
    })
    public ResponseEntity<BoardSaveResponseDto> save(@RequestPart(value = "file", required = false) List<MultipartFile> multipartFile,
                                                     @RequestPart(value = "request", required = false) BoardSaveRequestDto boardSaveRequestDto) throws IOException {
        return new ResponseEntity(boardService.save(multipartFile, boardSaveRequestDto), HttpStatus.OK);
    }
    @Operation(summary = "게시글 조회",
            description = "게시글을 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page){
        return new ResponseEntity(boardService.findAll(page), HttpStatus.OK);
    }
    @Operation(summary = "게시글 검색",
            description = "게시글을 keyword를 통해 검색 마찬가지로 10개씩 검색 결과 나옴 (페이지 시작 0부터)")
    @GetMapping("/search")
    @Parameters({
            @Parameter(name = "keyword", description = "검색 키워드", required = true),
            @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    })
    public ResponseEntity<List<BoardFindResponseDto>> search(@RequestParam(value = "keyword", required = false) String keyword,
                                                             @RequestParam(value = "page", defaultValue = "0") Integer page){
        return new ResponseEntity(boardService.search(keyword, page), HttpStatus.OK);
    }
}
