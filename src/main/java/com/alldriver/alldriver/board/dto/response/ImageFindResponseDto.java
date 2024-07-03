package com.alldriver.alldriver.board.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.alldriver.alldriver.board.domain.BoardImage;


public record ImageFindResponseDto(
        Long id,
        String url
) {

}
