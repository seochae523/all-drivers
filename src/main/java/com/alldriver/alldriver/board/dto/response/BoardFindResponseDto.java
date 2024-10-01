package com.alldriver.alldriver.board.dto.response;


import com.alldriver.alldriver.board.dto.query.SubLocationQueryDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardFindResponseDto {
    private Long id;
    private String title;
    private String userId;
    private LocalDateTime createdAt;
    private Integer bookmarked;
    private MainLocationFindResponseDto mainLocation;
    private List<SubLocationQueryDto> subLocations = new ArrayList<>();


    @QueryProjection
    public BoardFindResponseDto(Long id, String title, String userId, LocalDateTime createdAt, Integer bookmarked) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.createdAt = createdAt;
        this.bookmarked = bookmarked;
    }
}
