package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.service.BoardRetrieveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
@Tag(name = "board retrieve")
public class BoardRetrieveController {
    private final BoardRetrieveService boardRetrieveService;

    @Operation(description = "게시글을 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/board/all")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findAllBoards(@RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(boardRetrieveService.findAll(page));
    }
    @Operation(description = "게시글을 차량 id로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/board/cars")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findByCars(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "carIds") List<Long> carIds){
        return ResponseEntity.ok(boardRetrieveService.findByCars(page, carIds));
    }
    @Operation(description = "게시글을 직업 id로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/board/jobs")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findByJobs(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "jobIds") List<Long> jobIds){
        return ResponseEntity.ok(boardRetrieveService.findByJobs(page, jobIds));
    }
    @Operation(description = "게시글을 지역(구) id로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/board/subLocations")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findBySubLocations(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "subLocationIds") List<Long> subLocationIds){
        return ResponseEntity.ok(boardRetrieveService.findBySubLocations(page, subLocationIds));
    }
    @Operation(description = "게시글을 지역(시) 로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/board/mainLocation")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findByMainLocation(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "mainLocationId") Long mainLocationId){
        return ResponseEntity.ok(boardRetrieveService.findByMainLocation(page, mainLocationId));
    }
    @Operation(description = "게시글을 키워드로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/board/search")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> search(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                         @RequestParam(value = "keyword") String keyword){
        return ResponseEntity.ok(boardRetrieveService.search(page, keyword));
    }
    @Operation(description = "게시글을 user id 로 10개씩 조회 (페이지 시작 0부터)")
    @GetMapping("/board/userId")
    @Parameter(name = "page", description = "페이지 번호 기본 값 0")
    public ResponseEntity<List<BoardFindResponseDto>> findByUserId(@RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(boardRetrieveService.findByUserId(page));
    }

}
