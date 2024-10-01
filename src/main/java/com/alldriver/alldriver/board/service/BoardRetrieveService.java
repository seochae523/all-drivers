package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.response.*;

import java.util.List;

public interface BoardRetrieveService {
    PagingResponseDto<List<BoardFindResponseDto>> findAll(Integer page);
    BoardDetailResponseDto findDetailById(Long id);
    PagingResponseDto<List<BoardFindResponseDto>> findMyBookmarkedBoard(Integer page);
    PagingResponseDto<List<BoardFindResponseDto>> findBy(Integer page, List<Long> jobIds, List<Long> carIds, List<Long> subLocationIds, Long mainLocationId);

    List<BoardSearchResponseDto> search(Integer page, String keyword);
}
