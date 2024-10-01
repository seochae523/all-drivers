package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.query.BoardSearchCondition;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface BoardQueryDslRepository {
    Page<BoardFindResponseDto> search(BoardSearchCondition condition, Pageable pageable, String userId);
}
