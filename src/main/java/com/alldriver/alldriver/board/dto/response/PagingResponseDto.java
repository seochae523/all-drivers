package com.alldriver.alldriver.board.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponseDto<T> {
    private Integer currentPage;
    private Long totalElements;
    private Integer totalPage;
    private T data;
}
