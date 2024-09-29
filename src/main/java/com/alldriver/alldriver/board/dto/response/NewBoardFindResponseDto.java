package com.alldriver.alldriver.board.dto.response;

import com.alldriver.alldriver.board.dto.query.BoardFindJpqlResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewBoardFindResponseDto {
    private Long lastIdx;
    private List<BoardFindJpqlResponseDto> result;
}
