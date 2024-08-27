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

    @PostMapping("/save")
    @Operation(summary = "게시글 저장", description = "게시글을 form-data 타입을 통해 저장" +
            "</br> images :: multipart/form-data로 전송" +
            "</br> request :: application/json으로 전송")
    @Parameters({
            @Parameter(name="images", description = "이미지 첨부"),
            @Parameter(name="request", description = "게시글에 필요한 리퀘스트", required = true)
    })
    public ResponseEntity<BoardSaveResponseDto> save(@RequestPart(value = "images", required = false) List<MultipartFile> multipartFile,
                                                     @RequestPart(value = "request")
                                                     @Valid BoardSaveRequestDto boardSaveRequestDto) throws IOException {
        return ResponseEntity.ok(boardService.save(multipartFile, boardSaveRequestDto));
    }


    @PutMapping("/update")
    @Operation(summary = "게시글 업데이트", description = "게시글을 form-data 타입을 통해 업데이트" +
            "</br> images :: multipart/form-data로 전송" +
            "</br> request :: application/json으로 전송" +
            "</br> carInfos, jobInfos, locationInfos :: type = 삭제 원한다면 -1 추가하고 싶으면 0 / id = 추가 할 차종 id. type이 -1일때는 아무 값이나 넣으세요")
    @Parameters({
            @Parameter(name="images", description = "추가 할 이미지"),
            @Parameter(name="request", description = "게시글에 필요한 리퀘스트", required = true)
    })
    public ResponseEntity<BoardUpdateResponseDto> update(@RequestPart(value = "images", required = false) List<MultipartFile> multipartFile,
                                                         @RequestPart(value = "request")
                                                         @Valid BoardUpdateRequestDto boardUpdateRequestDto) throws IOException {
        return ResponseEntity.ok(boardService.update(multipartFile, boardUpdateRequestDto));
    }
    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<BoardDeleteResponseDto> delete(@PathVariable
                                                         @NotNull(message = ValidationError.Message.BOARD_ID_NOT_FOUND) Long boardId){
        return ResponseEntity.ok(boardService.delete(boardId));
    }
}
