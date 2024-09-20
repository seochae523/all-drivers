package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.response.ImageFindResponseDto;

import java.util.List;

public interface BoardRetrieveService {

    List<BoardFindResponseDto> findAll(Integer page);
    List<BoardFindResponseDto> findByCars(Integer page, List<Long> carIds);
    List<BoardFindResponseDto> findByJobs(Integer page ,List<Long> jobIds);

    List<BoardFindResponseDto> findBySubLocations(Integer page, List<Long> subLocationIds);

    List<BoardFindResponseDto> findByMainLocation(Integer page, Long mainLocationId);
    List<BoardFindResponseDto> findByUserId(Integer page);
    List<BoardFindResponseDto> search(Integer page,String keyword);

    List<BoardFindResponseDto> findByComplexParameters(Integer page, List<Long> carIds, List<Long> jobIds, List<Long> subLocationIds, Long mainLocationId);
    List<ImageFindResponseDto> findImageByBoardId(Long boardId);

    List<BoardFindResponseDto> findMyBookmarkedBoard(Integer page);
}
