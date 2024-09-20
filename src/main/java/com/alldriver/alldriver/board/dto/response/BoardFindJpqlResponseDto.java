package com.alldriver.alldriver.board.dto.response;

import com.alldriver.alldriver.board.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardFindJpqlResponseDto {
    private Board board;
    private Boolean bookmarked;
}
