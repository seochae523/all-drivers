package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.response.ImageFindResponseDto;
import com.alldriver.alldriver.board.service.BoardRetrieveService;
import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @GetMapping("/all")
    @Parameter(name = "page")
    public ResponseEntity<List<BoardFindResponseDto>> findAllBoards(@RequestParam(value = "page", defaultValue = "0")
                                                                    @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page){
        return ResponseEntity.ok(boardRetrieveService.findAll(page));
    }
    @Operation(summary = "게시글을 [차량 id]로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/cars")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findByCars(@RequestParam(value = "page", defaultValue = "0")
                                                                 @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND)Integer page,
                                                                 @RequestParam(value = "carIds")
                                                                 @NotNull(message = ValidationError.Message.CAR_ID_NOT_FOUND) List<Long> carIds){
        return ResponseEntity.ok(boardRetrieveService.findByCars(page, carIds));
    }
    @Operation(summary = "게시글을 [직업 id]로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/jobs")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findByJobs(@RequestParam(value = "page", defaultValue = "0")
                                                                 @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page,
                                                                 @RequestParam(value = "jobIds")
                                                                 @NotNull(message = ValidationError.Message.JOB_ID_NOT_FOUND) List<Long> jobIds){
        return ResponseEntity.ok(boardRetrieveService.findByJobs(page, jobIds));
    }
    @Operation(summary = "게시글을 [지역 - 구 id]로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/subLocations")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findBySubLocations(@RequestParam(value = "page", defaultValue = "0")
                                                                         @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page,
                                                                         @RequestParam(value = "subLocationIds")
                                                                         @NotNull(message = ValidationError.Message.SUB_LOCATION_ID_NOT_FOUND) List<Long> subLocationIds){
        return ResponseEntity.ok(boardRetrieveService.findBySubLocations(page, subLocationIds));
    }
    @Operation(summary = "게시글을 [지역 - 시 id]로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/mainLocation")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findByMainLocation(@RequestParam(value = "page", defaultValue = "0")
                                                                         @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page,
                                                                         @RequestParam(value = "mainLocationId")
                                                                         @NotNull(message = ValidationError.Message.MAIN_LOCATION_ID_NOT_FOUND) Long mainLocationId){
        return ResponseEntity.ok(boardRetrieveService.findByMainLocation(page, mainLocationId));
    }
    @Operation(summary = "게시글을 [키워드]로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/search")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> search(@RequestParam(value = "page", defaultValue = "0")
                                                             @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page,
                                                             @RequestParam(value = "keyword")
                                                             @NotBlank(message = ValidationError.Message.KEYWORD_NOT_FOUND) String keyword){
        return ResponseEntity.ok(boardRetrieveService.search(page, keyword));
    }
    @Operation(summary = "게시글을 [유저 id]로 10개씩 조회 (페이지 시작 0부터) - 유저 id는 token 삽입시 자동 추출")
    @GetMapping("/userId")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findByUserId(@RequestParam(value = "page", defaultValue = "0")
                                                                   @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page){
        return ResponseEntity.ok(boardRetrieveService.findByUserId(page));
    }
    @Operation(summary = "게시글을 [파라미터]로 10개씩 조회 (페이지 시작 0부터) " +
            "</br> [page 제외 파라미터들 기입 안하면 해당 카테고리 전체 조회입니다.]")
    @GetMapping("/parameters")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findByComplexParameters(@RequestParam(value = "page", defaultValue = "0")
                                                                              @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page,
                                                                              @RequestParam(value = "carIds", required = false) List<Long> carIds,
                                                                              @RequestParam(value = "jobIds", required = false) List<Long> jobIds,
                                                                              @RequestParam(value = "subLocationIds", required = false) List<Long> subLocationIds,
                                                                              @RequestParam(value = "mainLocationId", required = false) Long mainLocationId){
        return ResponseEntity.ok(boardRetrieveService.findByComplexParameters(page, carIds, jobIds, subLocationIds, mainLocationId));
    }
    @Operation(summary = "유저가 즐겨찾기 한 게시글을 조회 - 유저 id는 token 삽입시 자동 추출")
    @GetMapping("/bookmark")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findByUserBookmark(@RequestParam(value = "page", defaultValue = "0")
                                                                         @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page){
        return ResponseEntity.ok(boardRetrieveService.findMyBookmarkedBoard(page));
    }
    @Operation(summary = "게시글 이미지를 [board id]로 조회 ",
            description = "각 조회 이후 추가적인 이미지 조회를 위해 사용")
    @GetMapping("/images/{boardId}")
    public ResponseEntity<List<ImageFindResponseDto>> findImageByBoardId(@PathVariable
                                                                         @NotNull(message = ValidationError.Message.BOARD_ID_NOT_FOUND) Long boardId){
        return ResponseEntity.ok(boardRetrieveService.findImageByBoardId(boardId));
    }


}
