package com.alldriver.alldriver.board.dto.response;

import com.alldriver.alldriver.board.domain.Board;

import java.time.LocalDateTime;
import java.util.Date;

public class BoardDeleteResponseDto {
    private LocalDateTime date;
    private String content;
    private String title;


    public BoardDeleteResponseDto(Board board){
        this.date = board.getCreatedAt();
        this.content = board.getContent();
        this.title = board.getTitle();

    }
}
