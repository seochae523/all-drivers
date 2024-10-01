package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.response.*;
import com.alldriver.alldriver.board.service.BoardRetrieveService;
import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/board")
@Tag(name = "게시글 조회 관련 api")
@Validated
public class BoardRetrieveController {
    private final BoardRetrieveService boardRetrieveService;
    @Operation(summary = "게시글을 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/all/detail/{id}")
    public ResponseEntity<BoardDetailResponseDto> findDetailById(@PathVariable Long id){
        return ResponseEntity.ok(boardRetrieveService.findDetailById(id));
    }
    @Operation(summary = "게시글을 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/all/{page}")
    @Parameter(name = "page")
    public ResponseEntity<PagingResponseDto<List<BoardFindResponseDto>>> findAll(@Min(value = 0, message = ValidationError.Message.MINIMUM_PAGE_VALUE_ERROR)
                                                                                         @PathVariable Integer page){
        return ResponseEntity.ok(boardRetrieveService.findAll(page));
    }

    @Operation(summary = "유저가 즐겨찾기 한 게시글을 조회 - 유저 id는 token 삽입시 자동 추출")
    @GetMapping("/bookmark/{page}")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<PagingResponseDto<List<BoardFindResponseDto>>> findByUserBookmark(@Min(value = 0, message = ValidationError.Message.MINIMUM_PAGE_VALUE_ERROR)
                                                                            @PathVariable Integer page){
        return ResponseEntity.ok(boardRetrieveService.findMyBookmarkedBoard(page));
    }

    @Operation(summary = "게시글을 [키워드]로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/search/{page}")
    public ResponseEntity<List<BoardSearchResponseDto>> search(@Min(value = 0, message = ValidationError.Message.MINIMUM_PAGE_VALUE_ERROR)
                                                               @PathVariable Integer page,
                                                               @NotBlank(message = ValidationError.Message.KEYWORD_NOT_FOUND)
                                                               @RequestParam("keyword") String keyword){
        return ResponseEntity.ok(boardRetrieveService.search(page, keyword));
    }
    @Operation(summary = "게시글을 [파라미터]로 10개씩 조회 (페이지 시작 0부터) " +
            "</br> 없으면 안넣으면 됩니다.")
    @GetMapping("/by/{page}")
    public ResponseEntity<PagingResponseDto<List<BoardFindResponseDto>>> findBy(@Min(value = 0, message = ValidationError.Message.MINIMUM_PAGE_VALUE_ERROR)
                                                                                           @PathVariable Integer page,
                                                                                   @RequestParam(value = "carIds", required = false) List<Long> carIds,
                                                                                   @RequestParam(value = "jobIds", required = false) List<Long> jobIds,
                                                                                   @RequestParam(value = "subLocationIds", required = false) List<Long> subLocationIds,
                                                                                   @RequestParam(value = "mainLocationId", required = false) Long mainLocationId){
        return ResponseEntity.ok(boardRetrieveService.findBy(page, jobIds, carIds, subLocationIds, mainLocationId));
    }

}
