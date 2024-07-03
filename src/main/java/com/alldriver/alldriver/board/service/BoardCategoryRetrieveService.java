package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.dto.response.CarFindResponseDto;
import com.alldriver.alldriver.board.dto.response.JobFindResponseDto;
import com.alldriver.alldriver.board.dto.response.MainLocationFindResponseDto;
import com.alldriver.alldriver.board.dto.response.SubLocationFindResponseDto;

import java.util.List;

public interface BoardCategoryRetrieveService {
    List<JobFindResponseDto> findAllJobs();
    List<CarFindResponseDto> findAllCars();
    List<MainLocationFindResponseDto> findAllMainLocations();
    List<SubLocationFindResponseDto> findAllSubLocations();
    List<SubLocationFindResponseDto> findSubLocationsByMainLocation(Long id);
}
