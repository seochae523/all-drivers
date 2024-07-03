package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;

import java.util.List;

public interface BoardRetrieveService {
    List<BoardFindResponseDto> findAll(Integer page);
    List<BoardFindResponseDto> findByCars(Integer page, List<Long> carIds);
    List<BoardFindResponseDto> findByJobs(Integer page ,List<Long> jobIds);

    List<BoardFindResponseDto> findBySubLocations(Integer page, List<Long> subLocationIds);

    List<BoardFindResponseDto> findByMainLocation(Integer page, Long mainLocationId);
}
