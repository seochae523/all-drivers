package com.alldriver.alldriver.board.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.alldriver.alldriver.board.domain.Board;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardFindResponseDto {
    @Schema(description = "조회 결과 id 나중에 update, delete 할때 필요함", example = "3")
    private Long id;
    @Schema(description = "제목", example = "example")
    private String title;
    @Schema(description = "아이디", example = "example")
    private String userId;
    @Schema(description = "작성일", example = "2024-04-14T17:21:33.975Z")
    private LocalDateTime createdAt;
    @Schema(description = "이미지들", example = "images :[~~]")
    private Set<ImageFindResponseDto> images;
    @Schema(description = "급여", example = "1500000")
    private Integer payment;
    @Schema(description = "급여 형태", example = "월급")
    private String payType;
    @Schema(description = "지역")
    private List<String> places;
    @Schema(description = "직종")
    private List<String> jobs;
    @Schema(description = "차종")
    private List<String> cars;

    public BoardFindResponseDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.userId = board.getUser().getUserId();
        this.createdAt = board.getCreatedAt();
        this.payment = board.getPayment();
        this.payType = board.getPayType();
        if(board.getBoardImages().isEmpty()){
            this.images = new HashSet<>();
        }
        else {
            this.images = board.getBoardImages().stream()
                    .map(ImageFindResponseDto::new)
                    .collect(Collectors.toSet());
        }

    }
}
