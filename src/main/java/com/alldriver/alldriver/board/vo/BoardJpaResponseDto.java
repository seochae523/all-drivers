package com.alldriver.alldriver.board.vo;

import com.alldriver.alldriver.board.domain.Board;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public interface BoardJpaResponseDto {
    Long getBoardId();
    String getTitle();
    String getUserId();
    LocalDateTime getCreatedAt();

    Integer getBookmarked();
}
