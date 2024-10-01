package com.alldriver.alldriver.board.dto.query;


import com.fasterxml.jackson.annotation.JsonIgnore;

public record SubLocationQueryDto(@JsonIgnore Long boardId, Long id, String category, @JsonIgnore Long mainLocationId, @JsonIgnore String mainLocation) {
}
