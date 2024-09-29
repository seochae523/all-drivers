package com.alldriver.alldriver.board.dto.query;

import com.alldriver.alldriver.board.dto.query.subLocationQueryDto;
import com.alldriver.alldriver.board.dto.response.MainLocationFindResponseDto;
import com.alldriver.alldriver.board.vo.BoardJpaResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardFindJpqlResponseDto {
    private Long id;
    private String title;
    private String userId;
    private LocalDateTime createdAt;
    private Integer bookmarked;
    private MainLocationFindResponseDto mainLocation;
    private List<subLocationQueryDto> subLocations = new ArrayList<>();

    public BoardFindJpqlResponseDto(BoardJpaResponseDto boardJpaResponseDto) {
        this.id = boardJpaResponseDto.getBoardId();
        this.title = boardJpaResponseDto.getTitle();
        this.userId = boardJpaResponseDto.getUserId();
        this.createdAt = boardJpaResponseDto.getCreatedAt();
        this.bookmarked = boardJpaResponseDto.getBookmarked();
    }
}
